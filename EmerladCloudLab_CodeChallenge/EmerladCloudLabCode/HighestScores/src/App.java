import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.json.*;

public class App {
    public static void main(String[] args) throws Exception {
        // read data from the file
        String path = "/Users/joan/Downloads/ECL Highest Scores/example_input_data_1.data";
        String data = "{\n";
        int MaxHighestScores = 10;

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + ",\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        data += "}";

        JSONObject obj = new JSONObject(data);
        Set<String> keys = obj.keySet();
        PriorityQueue<Record> heap = new PriorityQueue<Record>((a, b) -> Long.compare(a.score, b.score));
        for (String k : keys) {
            JSONObject value = obj.getJSONObject(k);
            if (!value.has("id")) {
                System.out.println("An entry lack of id attribute.");
                System.out.println(value);
                return;
            }

            Record r = new Record(value.getString("id"), Long.parseLong(k));
            heap.add(r);

            if (heap.size() > MaxHighestScores)
                heap.poll();
        }

        List<Record> list = new ArrayList<>();
        while (!heap.isEmpty()) {
            list.add(heap.poll());
        }

        Collections.reverse(list);
        JSONArray res = new JSONArray();
        for (Record r : list) {
            JSONObject item = new JSONObject();
            item.put("id", r.id);
            item.put("score", r.score);
            res.put(item);
        }

        System.out.print(res);
    }
}

class Record {
    String id;
    long score;

    public Record(String id, long score) {
        this.id = id;
        this.score = score;
    }
}