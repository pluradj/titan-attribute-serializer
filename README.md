README
======

This example shows how to create and configure custom attribute serialization in Titan. Out of the box, Titan handles a limited set of simple types for serialization. Matthias [mentioned](https://groups.google.com/d/msg/aureliusgraphs/4VYJowNNqkw/d4Ue931YD7gJ) that previous versions of Titan allowed serialization for almost anything, but that proved to be a maintenance nightmare, especially for collections that contained arbitrary objects.

This `HashMapSerializer` uses default <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/serialization/index.html">Java object serialization</a> for the map and its contents. Keep in mind that the format is not optimized for compactness, like the <a href="http://s3.thinkaurelius.com/docs/titan/1.0.0/schema.html#d0e991">native Titan data types</a> and associated <a href="http://thinkaurelius.github.io/titan/javadoc/1.0.0/com/thinkaurelius/titan/graphdb/database/serialize/attribute/package-summary.html">serializers</a>. I recommend using the native Titan data types as much as possible.


References
----------

* [Titan Graph Database](http://titandb.io)
    * [Datatype and Attribute Serializer Configuration](http://s3.thinkaurelius.com/docs/titan/1.0.0/serializer.html)
    * [AttributeSerializer Javadoc](http://thinkaurelius.github.io/titan/javadoc/1.0.0/com/thinkaurelius/titan/core/attribute/AttributeSerializer.html)
    * [Titan Data Model](https://github.com/thinkaurelius/titan/wiki/Titan-Data-Model)
* [Titan mailing list](https://groups.google.com/d/msg/aureliusgraphs/4VYJowNNqkw/AkBpJPA5iM8J)
* [StackOverflow](http://stackoverflow.com/questions/31552043/titan-0-9-0-m2-custom-attribute-serializer-hashmap-as-a-property-value/31574075#31574075)


Prerequisites
-------------

* [Apache Maven 3.3.x](http://maven.apache.org/)
* [Java 8 Update 40+](https://www.java.com/)
* [Titan 1.0.0](http://s3.thinkaurelius.com/downloads/titan/titan-1.0.0-hadoop1.zip)


Build and Install
-----------------

```
mvn clean package
cp ./target/*.jar $TITAN_HOME/lib/
cp -r ./conf/* $TITAN_HOME/conf/
```


Attribute Serializer Configuration
----------------------------------

```
attributes.custom.attribute1.attribute-class=java.lang.StringBuffer
attributes.custom.attribute1.serializer-class=pluradj.titan.graphdb.database.serialize.attribute.StringBufferSerializer
attributes.custom.attribute2.attribute-class=java.util.HashMap
attributes.custom.attribute2.serializer-class=pluradj.titan.graphdb.database.serialize.attribute.HashMapSerializer
```


Create graph from Gremlin Console
---------------------------------

```
[vagrant@localhost titan-1.0.0-hadoop1]$ rm -rf ./db/
[vagrant@localhost titan-1.0.0-hadoop1]$ ./bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: aurelius.titan
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
00:11:35 INFO  org.apache.tinkerpop.gremlin.hadoop.structure.HadoopGraph  - HADOOP_GREMLIN_LIBS is set to: /tmp/titan-1.0.0-hadoop1/lib
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.tinkergraph
gremlin> graph = TitanFactory.open('./conf/attr-titan-berkeleyje.properties')
*** StringBufferSerializer constructor
*** HashMapSerializer constructor
==>standardtitangraph[berkeleyje:/tmp/titan-1.0.0-hadoop1/./conf/../db/berkeley]
gremlin> mgmt = graph.openManagement()
==>com.thinkaurelius.titan.graphdb.database.management.ManagementSystem@78010562
gremlin> hello = mgmt.makePropertyKey('hello').dataType(StringBuffer.class).cardinality(Cardinality.SINGLE).make()
==>hello
gremlin> poi = mgmt.makePropertyKey('poi').dataType(HashMap.class).cardinality(Cardinality.SINGLE).make()
==>poi
gremlin> mgmt.commit()
==>null
gremlin> v = graph.addVertex()
==>v[4240]
gremlin> v.property('hello', new StringBuffer('world'))
==>vp[hello->world]
gremlin> m = [] as HashMap
gremlin> m.name = 'rdu'; m.lat = 35.880; m.lon = -78.788; m.col = java.awt.Color.ORANGE
==>java.awt.Color[r=255,g=200,b=0]
gremlin> v.property('poi', m)
==>vp[poi->{col=java.awt.Color[]
gremlin> graph.tx().commit()
*** StringBufferSerializer write
*** HashMapSerializer write
==>null
gremlin> g = graph.traversal()
==>graphtraversalsource[standardtitangraph[berkeleyje:/tmp/titan-1.0.0-hadoop1/./conf/../db/berkeley], standard]
gremlin> vx = g.V().next()
00:13:27 WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
==>v[4240]
gremlin> sb = vx.values('hello').next()
*** StringBufferSerializer read
==>world
gremlin> sb.getClass()
==>class java.lang.StringBuffer
gremlin> hm = vx.values('poi').next()
*** HashMapSerializer read
==>col=java.awt.Color[r=255,g=200,b=0]
==>name=rdu
==>lon=-78.788
==>lat=35.880
gremlin> hm.getClass()
==>class java.util.HashMap
gremlin> col = hm.col
==>java.awt.Color[r=255,g=200,b=0]
gremlin> col.getClass()
==>class java.awt.Color
gremlin> :q
```


Host graph on Gremlin Server
----------------------------

```
[vagrant@localhost titan-1.0.0-hadoop1]$ ./bin/gremlin-server.sh ./conf/gremlin-server/attr-gremlin-server.yaml
+ '[' ./conf/gremlin-server/attr-gremlin-server.yaml = -i ']'
+ ARGS=./conf/gremlin-server/attr-gremlin-server.yaml
+ '[' 1 = 0 ']'
+ exec /opt/jdk1.8.0_60/bin/java -server -Dtitan.logdir=/tmp/titan-1.0.0-hadoop1/bin/../log -Dlog4j.configuration=conf/gremlin-server/log4j-server.properties -Xms32m -Xmx512m -javaagent:/tmp/titan-1.0.0-hadoop1/lib/jamm-0.3.0.jar -cp <classpath> org.apache.tinkerpop.gremlin.server.GremlinServer ./conf/gremlin-server/attr-gremlin-server.yaml
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
0    [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - 
         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----

125  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Configuring Gremlin Server from ./conf/gremlin-server/attr-gremlin-server.yaml
217  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics ConsoleReporter configured with report interval=180000ms
219  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics CsvReporter configured with report interval=180000ms to fileName=/tmp/gremlin-server-metrics.csv
402  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics JmxReporter configured with domain= and agentId=
404  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics Slf4jReporter configured with interval=180000ms and loggerName=org.apache.tinkerpop.gremlin.server.Settings$Slf4jReporterMetrics
1094 [main] INFO  com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration  - Generated unique-instance-id=7f0000017043-localhost-localdomain1
1126 [main] INFO  com.thinkaurelius.titan.diskstorage.Backend  - Initiated backend operations thread pool of size 16
*** StringBufferSerializer constructor
*** HashMapSerializer constructor
1277 [main] INFO  com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog  - Loaded unidentified ReadMarker start time 2015-11-13T05:15:10.557Z into com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog$MessagePuller@492691d7
1277 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Graph [graph] was successfully configured via [./conf/gremlin-server/attr-titan-berkeleyje-server.properties].
1277 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized Gremlin thread pool.  Threads in pool named with pattern gremlin-*
1706 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded nashorn ScriptEngine
2184 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded gremlin-groovy ScriptEngine
2697 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor  - Initialized gremlin-groovy ScriptEngine with scripts/empty-sample.groovy
2697 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized GremlinExecutor and configured ScriptEngines.
2704 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - A GraphTraversalSource is now bound to [g] with graphtraversalsource[standardtitangraph[berkeleyje:./db/berkeley], standard]
2823 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2824 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo-stringd with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2949 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0
2950 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0
3064 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Gremlin Server configured with worker thread pool of 1, gremlin pool of 8 and boss thread pool of 1.
3064 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Channel started at port 8182.
```


Connect to Gremlin Server from Gremlin Console
----------------------------------------------

```
[vagrant@localhost titan-1.0.0-hadoop1]$ ./bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: aurelius.titan
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/tmp/titan-1.0.0-hadoop1/lib/logback-classic-1.1.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
00:18:24 INFO  org.apache.tinkerpop.gremlin.hadoop.structure.HadoopGraph  - HADOOP_GREMLIN_LIBS is set to: /tmp/titan-1.0.0-hadoop1/lib
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.tinkergraph
gremlin> :remote connect tinkerpop.server ./conf/remote.yaml
==>Connected - localhost/127.0.0.1:8182
gremlin> :> g.V().properties()
==>vp[hello->world]
==>vp[poi->{col=java.awt.Color[]
gremlin> :> g.V().values('poi')
==>{col=java.awt.Color[r=255,g=200,b=0], name=rdu, lon=-78.788, lat=35.880}
gremlin> :> g.V().values('poi').next().col.getGreen()
==>200
gremlin> :remote close
==>Removed - Gremlin Server - [localhost/127.0.0.1:8182]
gremlin> :q
```


Gremlin Server output from Gremlin Console session
--------------------------------------------------

```
204820 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the standard OpProcessor.
204821 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the control OpProcessor.
204824 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the session OpProcessor.
204903 [gremlin-server-exec-1] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** StringBufferSerializer read
*** HashMapSerializer read
211105 [gremlin-server-exec-2] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** HashMapSerializer read
216459 [gremlin-server-exec-3] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** HashMapSerializer read
```
