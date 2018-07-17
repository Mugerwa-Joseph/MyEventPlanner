package com.example.linweiran.myeventplanner;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by linweiran on 9/10/16.
 */
public class ParseResponse {

    private static final String DEBUG_TAG = ParseResponse.class.getName();

    class DistanceResult {

        private String topStatus;
        private String elementStatus;
        private int duration;

        public DistanceResult() {
            this.topStatus = "INVALID";
            this.elementStatus = "INVALID";
        }

        public String getTopStatus() {
            return topStatus;
        }

        public void setTopStatus(String topStatus) {
            this.topStatus = topStatus;
        }

        public String getElementStatus() {
            return elementStatus;
        }

        public void setElementStatus(String elementStatus) {
            this.elementStatus = elementStatus;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    DistanceResult distanceResult;

    public ParseResponse() {
        this.distanceResult = new DistanceResult();
    }

    public DistanceResult getDistance() {
        return distanceResult;
    }

    public void loadJsonStream(InputStream in) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try {
            parseRows(jsonReader);
        }
        finally {
            jsonReader.close();
        }
    }

    private void parseRows(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            String name = jsonReader.nextName();
            if (name.equals("rows"))
                parseRowArray(jsonReader);
            else if (name.equals("status"))
                this.distanceResult.setTopStatus(jsonReader.nextString());
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }

    private void parseRowArray(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext())

        {
            readRow(jsonReader);
        }
        jsonReader.endArray();
    }

    private void readRow(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            String name = jsonReader.nextName();
            if (name.equals("elements"))
                readElementsArray(jsonReader);
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }

    private void readElementsArray(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext())
            readElement(jsonReader);
        jsonReader.endArray();
    }

    private void readElement(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            String name = jsonReader.nextName();
            if (name.equals("duration"))
                readDuration(jsonReader);
            else if (name.equals("status"))
                this.distanceResult.setElementStatus(jsonReader.nextString());
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }

    private void readDuration(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext())
        {
            String name = jsonReader.nextName();
            if (name.equals("value"))
                this.distanceResult.setDuration(jsonReader.nextInt());
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }



}
