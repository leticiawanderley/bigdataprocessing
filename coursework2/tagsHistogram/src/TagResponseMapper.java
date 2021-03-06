import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import utils.MRDPUtils;
import utils.IntArrayPair;
import java.util.Map;

public class TagResponseMapper extends Mapper<Object, Text, Text, IntArrayPair> { 
    private final IntWritable one = new IntWritable(1);
    private final IntWritable zero = new IntWritable(0);
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());
	IntArrayPair result = new IntArrayPair();
	if ("2".equals(parsed.get("PostTypeId"))) {
		result.set(one, new ArrayWritable(new String[0]));
		context.write(new Text(parsed.get("ParentId")), result);	
	} else if ("1".equals(parsed.get("PostTypeId"))) {
		String[] tags = getTags(parsed.get("Tags"));
		result.set(zero, new ArrayWritable(tags));
		context.write(new Text(parsed.get("Id")), result);	
	}
    }

    private String[] getTags(String tags){
    	tags = tags.replace("&lt;", "").replace("&gt", "");
	return tags.split(";");
    }	
} 
