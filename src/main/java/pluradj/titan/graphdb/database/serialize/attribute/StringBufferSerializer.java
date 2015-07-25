package pluradj.titan.graphdb.database.serialize.attribute;

import com.thinkaurelius.titan.core.attribute.AttributeSerializer;
import com.thinkaurelius.titan.diskstorage.ScanBuffer;
import com.thinkaurelius.titan.diskstorage.WriteBuffer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.StringSerializer;

public class StringBufferSerializer implements AttributeSerializer<StringBuffer> {
    // leverage Titan's StringSerializer to integrate with its attribute serialization flow
    private StringSerializer serializer;

    public StringBufferSerializer() {
        System.out.println("*** StringBufferSerializer constructor");
        serializer = new StringSerializer();
    }

    @Override
    public StringBuffer read(ScanBuffer buffer) {
        System.out.println("*** StringBufferSerializer read");
        String str = serializer.read(buffer);
        return new StringBuffer(str);
    }

    @Override
    public void write(WriteBuffer buffer, StringBuffer attribute) {
        System.out.println("*** StringBufferSerializer write");
        serializer.write(buffer, attribute.toString());
    }
}
