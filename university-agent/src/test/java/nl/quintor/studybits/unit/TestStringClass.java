package nl.quintor.studybits.unit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestStringClass {

    public static String jsonTestString = "{\n" +
            "  \"id\": 2102241,\n" +
            "  \"name\": \"T. van den Bergh\",\n" +
            "  \"Studentnummer\": 2102241,\n" +
            "  \"Voornamen\": \"Tijn\",\n" +
            "  \"Roepnaam\": \"Tijn\",\n" +
            "  \"Geboortedatum\": \"06-11-1995\",\n" +
            "  \"Geboorteplaats\": \"Veldhoven\",\n" +
            "  \"Opleiding\": \"Informatica\",\n" +
            "  \"E-mailadres\": \"Fakeemail@avans.nl\",\n" +
            "  \"inschrijvingen\": [\n" +
            "    {\n" +
            "      \"name\": \"INFORMATICA-BACHELOR\",\n" +
            "      \"type\": \"Bachelor\",\n" +
            "      \"judicium\": true,\n" +
            "      \"completionDate\": \"31/05/2018\",\n" +
            "      \"totalEC\": 240,\n" +
            "      \"fases\": [\n" +
            "        {\n" +
            "          \"faseCode\": \"IMIN-DV-PROP-15\",\n" +
            "          \"judicium\": true,\n" +
            "          \"cohort\": \"2015\",\n" +
            "          \"type\": \"propedeuse\",\n" +
            "          \"completionDate\": \"07/07/2016\",\n" +
            "          \"totalEC\": 60,\n" +
            "          \"receivedEC\": 60,\n" +
            "          \"hasTranscript\": true,\n" +
            "          \"grades\": [\n" +
            "            {\n" +
            "              \"courseCode\": \"X-C++-19\",\n" +
            "              \"courseName\": \"C++ assessments\",\n" +
            "              \"Grade\": \"7.9\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"I-C#--19\",\n" +
            "              \"courseName\": \"C# assessments\",\n" +
            "              \"Grade\": \"8.5\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"I-SQL-19\",\n" +
            "              \"courseName\": \"SQL databases\",\n" +
            "              \"Grade\": \"6.0\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"I-SOA-Architectuur-19\",\n" +
            "              \"courseName\": \"SOA Architectuur\",\n" +
            "              \"Grade\": \"8\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"faseCode\": \"IMIN-DV-KERN-15\",\n" +
            "          \"judicium\": \"geslaagd\",\n" +
            "          \"cohort\": \"2016\",\n" +
            "          \"completionDate\": \"07-07-2016\",\n" +
            "          \"totalEC\": 180,\n" +
            "          \"receivedEC\": 180,\n" +
            "          \"hasTranscript\": false,\n" +
            "          \"grades\": [\n" +
            "            {\n" +
            "              \"courseCode\": \"X-C++-19\",\n" +
            "              \"courseName\": \"C++ assessments\",\n" +
            "              \"Grade\": \"7.9\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"X-C++-19\",\n" +
            "              \"courseName\": \"C++ assessments\",\n" +
            "              \"Grade\": \"7.9\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"X-C++-19\",\n" +
            "              \"courseName\": \"C++ assessments\",\n" +
            "              \"Grade\": \"7.9\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"courseCode\": \"X-C++-19\",\n" +
            "              \"courseName\": \"C++ assessments\",\n" +
            "              \"Grade\": \"7.9\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"grades\": [\n" +
            "        {\n" +
            "          \"courseCode\": \"X-C++-19\",\n" +
            "          \"courseName\": \"C++ assessments\",\n" +
            "          \"Grade\": \"7.9\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"X-C++-19\",\n" +
            "          \"courseName\": \"C++ assessments\",\n" +
            "          \"Grade\": \"7.9\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"X-C++-19\",\n" +
            "          \"courseName\": \"C++ assessments\",\n" +
            "          \"Grade\": \"7.9\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"X-C++-19\",\n" +
            "          \"courseName\": \"C++ assessments\",\n" +
            "          \"Grade\": \"7.9\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"X-C++-19\",\n" +
            "          \"courseName\": \"C++ assessments\",\n" +
            "          \"Grade\": \"7.9\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"I-C#--19\",\n" +
            "          \"courseName\": \"C# assessments\",\n" +
            "          \"Grade\": \"8.5\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"I-SQL-19\",\n" +
            "          \"courseName\": \"SQL databases\",\n" +
            "          \"Grade\": \"6.0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"courseCode\": \"I-SOA-Architectuur-19\",\n" +
            "          \"courseName\": \"SOA Architectuur\",\n" +
            "          \"Grade\": \"8\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"voortgang\": [\n" +
            "    {\n" +
            "      \"name\": \"informatica-propedeuse\",\n" +
            "      \"accomplished\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"informatica-hoofdfase\",\n" +
            "      \"accomplished\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"informatica-compleet\",\n" +
            "      \"accomplished\": false\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
