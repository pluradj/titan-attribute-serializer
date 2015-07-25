package pluradj.titan.graphdb.database.serialize.attribute;

import static org.junit.Assert.*;
import org.junit.Test;

import com.thinkaurelius.titan.graphdb.database.serialize.DataOutput;
import com.thinkaurelius.titan.graphdb.database.serialize.StandardSerializer;

import com.thinkaurelius.titan.diskstorage.ReadBuffer;

public class StringBufferSerializerTest {

    private StandardSerializer getStandardSerializer() {
        // register the custom attribute serializer with the Titan standard serializer
        StandardSerializer serialize = new StandardSerializer();
        serialize.registerClass(1, StringBuffer.class, new StringBufferSerializer());
        assertTrue(serialize.validDataType(StringBuffer.class));
        return serialize;
    }

    @Test
    public void writeReadObjectNotNull() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        StringBuffer sb = new StringBuffer("foo");
        DataOutput out = serialize.getDataOutput(128);
        out.writeObjectNotNull(sb);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        StringBuffer read = serialize.readObjectNotNull(b, StringBuffer.class);

        // make sure they are equal
        assertEquals(sb.toString(), read.toString());
    }

    @Test
    public void writeReadClassAndObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        StringBuffer sb = new StringBuffer("foo");
        DataOutput out = serialize.getDataOutput(128);
        out.writeClassAndObject(sb);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        StringBuffer read = (StringBuffer) serialize.readClassAndObject(b);

        // make sure they are equal
        assertEquals(sb.toString(), read.toString());
    }

    @Test
    public void writeReadObject() {
        StandardSerializer serialize = getStandardSerializer();

        // use the serializer to write the object
        StringBuffer sb = new StringBuffer("foo");
        DataOutput out = serialize.getDataOutput(128);
        out.writeObject(sb, StringBuffer.class);

        // use the serializer to read the object
        ReadBuffer b = out.getStaticBuffer().asReadBuffer();
        StringBuffer read = serialize.readObject(b, StringBuffer.class);

        // make sure they are equal
        assertEquals(sb.toString(), read.toString());
    }

}
