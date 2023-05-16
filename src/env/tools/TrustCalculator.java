package tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cartago.*;

public class TrustCalculator extends Artifact {

  @OPERATION
  public void calculateMostTrustworthyAgent(Object[] interactionRatings, Object[] certificationRatings,
      Object[] witnessRatings, OpFeedbackParam<Integer> trustworthyAgent) {
    // Create maps to store the sum and count for each sensing agent for each rating type
    Map<String, Double> sumMapInteraction = new HashMap<>();
    Map<String, Double> sumMapCertification = new HashMap<>();
    Map<String, Double> sumMapWitness = new HashMap<>();

    Map<String, Integer> countMapInteraction = new HashMap<>();
    Map<String, Integer> countMapCertification = new HashMap<>();
    Map<String, Integer> countMapWitness = new HashMap<>();

    // Process each rating type
    processRatings(interactionRatings, sumMapInteraction, countMapInteraction);
    processRatings(certificationRatings, sumMapCertification, countMapCertification);
    processRatings(witnessRatings, sumMapWitness, countMapWitness);

    // Create a set of all agents
    Set<String> allAgents = new HashSet<>();
    allAgents.addAll(sumMapInteraction.keySet());
    allAgents.addAll(sumMapCertification.keySet());
    allAgents.addAll(sumMapWitness.keySet());

    // Calculate the weighted average for each agent
    String agentWithHighestScore = "";
    double highestScore = Double.MIN_VALUE;
    for (String agent : allAgents) {
      double interactionAverage = sumMapInteraction.getOrDefault(agent, 0.0)
          / countMapInteraction.getOrDefault(agent, 1);
      double certificationAverage = sumMapCertification.getOrDefault(agent, 0.0)
          / countMapCertification.getOrDefault(agent, 1);
      double witnessAverage = sumMapWitness.getOrDefault(agent, 0.0) / countMapWitness.getOrDefault(agent, 1);

      // System.out.println(agent);
      // System.out.println(interactionAverage);
      // System.out.println(certificationAverage);
      // System.out.println(countMapCertification);

      double score = interactionAverage / 3 + certificationAverage / 3 + witnessAverage / 3;
      if (score > highestScore) {
        highestScore = score;
        agentWithHighestScore = agent;
      }
    }

    int lastCharacterAsInt = Character
        .getNumericValue(agentWithHighestScore.charAt(agentWithHighestScore.length() - 1));
    trustworthyAgent.set(lastCharacterAsInt);
  }

  private void processRatings(Object[] ratings, Map<String, Double> sumMap, Map<String, Integer> countMap) {
    String ratingsString = Arrays.deepToString(ratings);
    String[] pairs = ratingsString.substring(1, ratingsString.length() - 1).split("\\], \\[");

    for (String pair : pairs) {
      String[] values = pair.split(", ");

      String agent = values[0].replaceAll("\\[", "");
      String valueString = values[1].replaceAll("[^\\d.-]", "");
      double value = Double.parseDouble(valueString);

      sumMap.put(agent, sumMap.getOrDefault(agent, 0.0) + value);
      countMap.put(agent, countMap.getOrDefault(agent, 0) + 1);
    }
  }

  // IMPLEMENTATION FOR TASK 1 & 2
  // @OPERATION
  // public void calculateMostTrustworthyAgent(Object[] trustRatings,
  // OpFeedbackParam<Integer> trustworthyAgent) {
  // // Convert trustRatings to string
  // String trustRatingsString = Arrays.deepToString(trustRatings);
  // String[] pairs = trustRatingsString.substring(1, trustRatingsString.length()
  // - 1).split("\\], \\[");

  // // Create a map to store the sum and count for each sensing agent
  // Map<String, Double> sumMap = new HashMap<>();
  // Map<String, Integer> countMap = new HashMap<>();

  // for (String pair : pairs) {
  // // Split the pair into string and double values
  // String[] values = pair.split(", ");

  // String agent = values[0].replaceAll("\\[", "");
  // String valueString = values[1].replaceAll("[^\\d.-]", "");
  // double value = Double.parseDouble(valueString);

  // // Update the sum and count for the sensing agent
  // sumMap.put(agent, sumMap.getOrDefault(agent, 0.0) + value);
  // countMap.put(agent, countMap.getOrDefault(agent, 0) + 1);
  // }

  // Map<String, Double> averageMap = new HashMap<>();
  // for (String agent : sumMap.keySet()) {
  // double sum = sumMap.get(agent);
  // int count = countMap.get(agent);
  // double average = sum / count;
  // averageMap.put(agent, average);
  // }

  // // Find the sensing agent with the highest average
  // String agentWithHighestAverage = "";
  // double highestAverage = Double.MIN_VALUE;
  // for (String agent : averageMap.keySet()) {
  // double average = averageMap.get(agent);
  // if (average > highestAverage) {
  // highestAverage = average;
  // agentWithHighestAverage = agent;
  // }
  // }

  // int lastCharacterAsInt =
  // Character.getNumericValue(agentWithHighestAverage.charAt(agentWithHighestAverage.length()
  // - 1));
  // trustworthyAgent.set(lastCharacterAsInt);
  // }

}
