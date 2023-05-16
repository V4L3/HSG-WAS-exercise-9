package tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cartago.*;

public class TrustCalculator extends Artifact {

  @OPERATION
  public void calculateTrustAverage(Object[] trustRatings, OpFeedbackParam<Integer> trustworthyAgent) {
    // Convert trustRatings to string
    String trustRatingsString = Arrays.deepToString(trustRatings);
    String[] pairs = trustRatingsString.substring(1, trustRatingsString.length() - 1).split("\\], \\[");

    // Create a map to store the sum and count for each sensing agent
    Map<String, Double> sumMap = new HashMap<>();
    Map<String, Integer> countMap = new HashMap<>();

    for (String pair : pairs) {
      // Split the pair into string and double values
      String[] values = pair.split(", ");

      String agent = values[0].replaceAll("\\[", "");
      String valueString = values[1].replaceAll("[^\\d.-]", "");
      double value = Double.parseDouble(valueString);

      // Update the sum and count for the sensing agent
      sumMap.put(agent, sumMap.getOrDefault(agent, 0.0) + value);
      countMap.put(agent, countMap.getOrDefault(agent, 0) + 1);
    }

    Map<String, Double> averageMap = new HashMap<>();
    for (String agent : sumMap.keySet()) {
      double sum = sumMap.get(agent);
      int count = countMap.get(agent);
      double average = sum / count;
      averageMap.put(agent, average);
    }

    // Find the sensing agent with the highest average
    String agentWithHighestAverage = "";
    double highestAverage = Double.MIN_VALUE;
    for (String agent : averageMap.keySet()) {
      double average = averageMap.get(agent);
      if (average > highestAverage) {
        highestAverage = average;
        agentWithHighestAverage = agent;
      }
    }
    
    int lastCharacterAsInt = Character.getNumericValue(agentWithHighestAverage.charAt(agentWithHighestAverage.length() - 1));
    trustworthyAgent.set(lastCharacterAsInt);
  }

}
