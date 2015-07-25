README
======

This example shows how to create and configure custom attribute serialization in Titan. Out of the box, Titan handles a limited set of simple types for serialization. Matthias [mentioned](https://groups.google.com/d/msg/aureliusgraphs/4VYJowNNqkw/d4Ue931YD7gJ) that previous versions of Titan allowed serialization for almost anything, but that proved to be a maintenance nightmare, especially for collections that contained arbitrary objects.

This `HashMapSerializer` uses default <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/serialization/index.html">Java object serialization</a> for the map and its contents. Keep in mind that the format is not optimized for compactness, like the <a href="http://s3.thinkaurelius.com/docs/titan/0.9.0-M2/schema.html#d0e985">native Titan data types</a> and associated <a href="http://thinkaurelius.github.io/titan/javadoc/current/com/thinkaurelius/titan/graphdb/database/serialize/attribute/package-summary.html">serializers</a>. I recommend using the native Titan data types as much as possible.


References
----------

* [Titan Graph Database](http://titandb.io)
    * [Datatype and Attribute Serializer Configuration](http://s3.thinkaurelius.com/docs/titan/0.9.0-M2/serializer.html)
    * [AttributeSerializer Javadoc](http://thinkaurelius.github.io/titan/javadoc/current/com/thinkaurelius/titan/core/attribute/AttributeSerializer.html)
    * [Titan Data Model](https://github.com/thinkaurelius/titan/wiki/Titan-Data-Model)
* [Titan mailing list](https://groups.google.com/d/msg/aureliusgraphs/4VYJowNNqkw/AkBpJPA5iM8J)
* [StackOverflow](http://stackoverflow.com/questions/31552043/titan-0-9-0-m2-custom-attribute-serializer-hashmap-as-a-property-value/31574075#31574075)


Prerequisites
-------------

* [Apache Maven 3.3.x](http://maven.apache.org/)
* [Java 8 Update 40+](https://www.java.com/)
* [Titan 0.9.0-M2](http://s3.thinkaurelius.com/downloads/titan/titan-0.9.0-M2-hadoop1.zip)


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
[vagrant@localhost titan-0.9.0-M2-hadoop1]$ rm -rf ./db/
[vagrant@localhost titan-0.9.0-M2-hadoop1]$ ./bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
06:19:25 INFO  org.apache.tinkerpop.gremlin.hadoop.structure.HadoopGraph  - HADOOP_GREMLIN_LIBS is set to: /home/vagrant/titan-0.9.0-M2-hadoop1/bin/../lib
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.tinkergraph
plugin activated: aurelius.titan
gremlin> graph = TitanFactory.open('./conf/attr-titan-berkeleyje.properties')
*** StringBufferSerializer constructor
*** HashMapSerializer constructor
==>standardtitangraph[berkeleyje:/home/vagrant/titan-0.9.0-M2-hadoop1/./conf/../db/berkeley]
gremlin> mgmt = graph.openManagement()
==>com.thinkaurelius.titan.graphdb.database.management.ManagementSystem@778d82e9
gremlin> hello = mgmt.makePropertyKey('hello').dataType(StringBuffer.class).cardinality(Cardinality.SINGLE).make()
==>hello
gremlin> poi = mgmt.makePropertyKey('poi').dataType(HashMap.class).cardinality(Cardinality.SINGLE).make()
==>poi
gremlin> mgmt.commit()
==>null
gremlin> v = graph.addVertex()
==>v[4272]
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
==>graphtraversalsource[standardtitangraph[berkeleyje:/home/vagrant/titan-0.9.0-M2-hadoop1/./conf/../db/berkeley], standard]
gremlin> vx = g.V().next()
06:19:50 WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
==>v[4272]
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
[vagrant@localhost titan-0.9.0-M2-hadoop1]$ ./bin/gremlin-server.sh ./conf/gremlin-server/attr-gremlin-server.yaml
+ '[' ./conf/gremlin-server/attr-gremlin-server.yaml = -i ']'
+ ARGS=./conf/gremlin-server/attr-gremlin-server.yaml
+ '[' 1 = 0 ']'
+ exec java -server -Dtitan.logdir=/home/vagrant/titan-0.9.0-M2-hadoop1/bin/../log -Dlog4j.configuration=conf/gremlin-server/log4j-server.properties -Xms32m -Xmx512m -javaagent:/home/vagrant/titan-0.9.0-M2-hadoop1/bin/../lib/jamm-0.2.5.jar -cp <classpath> org.apache.tinkerpop.gremlin.server.GremlinServer ./conf/gremlin-server/attr-gremlin-server.yaml
0    [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  -
         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----

149  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Configuring Gremlin Server from ./conf/gremlin-server/attr-gremlin-server.yaml
234  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics ConsoleReporter configured with report interval=180000ms
238  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics CsvReporter configured with report interval=180000ms to fileName=/tmp/gremlin-server-metrics.csv
335  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics JmxReporter configured with domain= and agentId=
337  [main] INFO  org.apache.tinkerpop.gremlin.server.util.MetricManager  - Configured Metrics Slf4jReporter configured with interval=180000ms and loggerName=org.apache.tinkerpop.gremlin.server.Settings$Slf4jReporterMetrics
1029 [main] INFO  com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration  - Generated unique-instance-id=7f0000013366-localhost-localdomain1
1072 [main] INFO  com.thinkaurelius.titan.diskstorage.Backend  - Initiated backend operations thread pool of size 4
*** StringBufferSerializer constructor
*** HashMapSerializer constructor
1190 [main] INFO  com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog  - Loaded unidentified ReadMarker start time 2015-07-25T06:20:48.836Z into com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog$MessagePuller@632ceb35
1191 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Graph [graph] was successfully configured via [./conf/gremlin-server/attr-titan-berkeleyje-server.properties].
1191 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Initialized Gremlin thread pool.  Threads in pool named with pattern gremlin-*
1683 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded nashorn ScriptEngine
2253 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded gremlin-groovy ScriptEngine
2989 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor  - Initialized gremlin-groovy ScriptEngine with scripts/empty-sample.groovy
2989 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Initialized GremlinExecutor and configured ScriptEngines.
2991 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - A GraphTraversalSource is now bound to [g] with graphtraversalsource[standardtitangraph[berkeleyje:./db/berkeley], standard]
3051 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
3051 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo-stringd with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
3266 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0
3267 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0
3396 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Gremlin Server configured with worker thread pool of 1, gremlin pool of 8 and boss thread pool of 1.
3397 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Channel started at port 8182.
```


Connect to Gremlin Server from Gremlin Console
----------------------------------------------

```
[vagrant@localhost titan-0.9.0-M2-hadoop1]$ ./bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
plugin activated: tinkerpop.server
plugin activated: tinkerpop.utilities
03:21:31 INFO  org.apache.tinkerpop.gremlin.hadoop.structure.HadoopGraph  - HADOOP_GREMLIN_LIBS is set to: /home/vagrant/titan-0.9.0-M2-hadoop1/bin/../lib
plugin activated: tinkerpop.hadoop
plugin activated: tinkerpop.tinkergraph
plugin activated: aurelius.titan
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
33686 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the standard OpProcessor.
33687 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the control OpProcessor.
33689 [gremlin-server-worker-1] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the session OpProcessor.
33822 [gremlin-server-exec-2] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** StringBufferSerializer read
*** HashMapSerializer read
42923 [gremlin-server-exec-5] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** HashMapSerializer read
51954 [gremlin-server-exec-7] WARN  com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx  - Query requires iterating over all vertices [()]. For better performance, use indexes
*** HashMapSerializer read
```
