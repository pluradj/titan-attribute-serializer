graph = TitanFactory.open('./conf/attr-titan-berkeleyje.properties')
mgmt = graph.openManagement()
hello = mgmt.makePropertyKey('hello').dataType(StringBuffer.class).cardinality(Cardinality.SINGLE).make()
poi = mgmt.makePropertyKey('poi').dataType(HashMap.class).cardinality(Cardinality.SINGLE).make()
mgmt.commit()
v = graph.addVertex()
v.property('hello', new StringBuffer('world'))
m = [] as HashMap
m.name = 'rdu'; m.lat = 35.880; m.lon = -78.788; m.col = java.awt.Color.ORANGE
v.property('poi', m)
graph.tx().commit()
g = graph.traversal()
vx = g.V().next()
sb = vx.values('hello').next()
sb.getClass()
hm = vx.values('poi').next()
hm.getClass()
col = hm.col
col.getClass()
