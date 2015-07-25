package pluradj.titan.graphdb.database.serialize.attribute;

import static org.junit.Assert.*;
import org.junit.Test;

import com.thinkaurelius.titan.graphdb.database.serialize.DataOutput;
import com.thinkaurelius.titan.graphdb.database.serialize.StandardSerializer;

import com.thinkaurelius.titan.diskstorage.ReadBuffer;

import java.util.HashMap;

public class HashMapSerializerTest {

    private StandardSerializer getStandardSerializer() {
        // register the custom attribute serializer with the Titan standard serializer
        StandardSerializer serialize = new StandardSerializer();
        serialize.registerClass(2, HashMap.class, new HashMapSerializer());
        assertTrue(serialize.validDataType(HashMap.class));
        return serialize;
    }

    @Test
    public void writeReadObjectNotNull() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("one",1);
        DataOutput out = serialize.getDataOutput(128);
        out.writeObjectNotNull(map);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        HashMap<String,Integer> read = (HashMap<String,Integer>) serialize.readObjectNotNull(b, HashMap.class);

        // make sure they are equal
        assertEquals(map.size(), read.size());
        assertEquals(map.get("one"), read.get("one"));
    }

    @Test
    public void writeReadClassAndObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("one",1);
        DataOutput out = serialize.getDataOutput(128);
        out.writeClassAndObject(map);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        HashMap<String,Integer> read = (HashMap<String,Integer>) serialize.readClassAndObject(b);

        // make sure they are equal
        assertEquals(map.size(), read.size());
        assertEquals(map.get("one"), read.get("one"));
    }

    @Test
    public void writeReadObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("one",1);
        DataOutput out = serialize.getDataOutput(128);
        out.writeObject(map, HashMap.class);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        HashMap<String,Integer> read = (HashMap<String,Integer>) serialize.readObject(b, HashMap.class);

        // make sure they are equal
        assertEquals(map.size(), read.size());
        assertEquals(map.get("one"), read.get("one"));
    }

}
