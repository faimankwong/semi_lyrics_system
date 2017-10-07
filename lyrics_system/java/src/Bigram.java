import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Scanner;

/**
 This is the clase to get the bigram and goodTuringSmoothedProbability
 * */
public class Bigram
{
    public Set<String> samples;
    public  HashMap<String, HashMap<String, Double>> counts;
    public HashMap<String, Double> unigramCounts;
    public final String START = ":S";
    private String bigram="";
    // For add-one smoothing
    public Set<String> wordSet; // Used to find the vocabulary
    public double vocabSize; // Size of the vocabulary

    // For Good Turing Smoothing
    public double numTrainingBigrams; // The size of the training set (# non-distinct words)
    public HashMap<Double, Double> numberOfBigramsWithCount; // The number of bigrams that occur x times
    public boolean goodTuringCountsAvailable = false; // True when good turing counts are available



    public static void main (String[] args) throws  Exception
    {



    }

    public Bigram(Set<String> samples)
    {
        this.samples = samples;
        this.counts = new HashMap<String, HashMap<String, Double>>();
        this.unigramCounts = new HashMap<String, Double>();

        this.wordSet = new HashSet<String>();

        this.numberOfBigramsWithCount = new HashMap<Double, Double>();
    }

    public void train()
    {
        // Regexp to match words (starting with optional apos) or any punctuation (with probably extra escaping)
        String regexp = "('?\\S+|\\S)";
        Pattern pattern = Pattern.compile(regexp);
        for (String sample : samples) {
            System.out.println(sample);
            Matcher matcher = pattern.matcher(sample);
            String previousWord = START; // originally set to beginning-of-sentence marker
            while (matcher.find()) {
                // Set unigram counts (for word1)
                double unigramCount = 0.0;
                if (unigramCounts.containsKey(previousWord)) {
                    unigramCount = unigramCounts.get(previousWord);
                }
                unigramCounts.put(previousWord, unigramCount+1.0);

                // Get the new match (word2)
                String match = matcher.group();
                wordSet.add(match);

                // Get access to (or create) the count map for word1.
                HashMap<String, Double> innerCounts;
                if (counts.containsKey(previousWord)) {

                    innerCounts = counts.get(previousWord);
                    //System.out.println(innerCounts);
                } else {
                    innerCounts = new HashMap<String, Double>();
                    counts.put(previousWord, innerCounts);
                }

                // Add to the size of the training set for gt-smoothing
                numTrainingBigrams += 1;

                // Set bigram counts
                double count = 0.0;
                if (innerCounts.containsKey(match)) {
                    count = innerCounts.get(match);

                    // Decrement the number of bigrams with old count for gt-smoothing
                    numberOfBigramsWithCount.put(count,
                            numberOfBigramsWithCount.get(count) - 1.0);
                }
                innerCounts.put(match, count+1.0);
                //System.out.println(innerCounts);
                // Increment the number of bigrams with the new count for gt-smoothing
                if (!numberOfBigramsWithCount.containsKey(count+1.0)) {
                    numberOfBigramsWithCount.put(count+1.0, 1.0);
                } else {
                    numberOfBigramsWithCount.put(count+1.0,
                            numberOfBigramsWithCount.get(count+1.0) + 1.0);
                }

                // Update previousWord
                previousWord = match;
            }
        }

        vocabSize = wordSet.size();
    }

    public double count(String word1, String word2)
    {
        if (counts.containsKey(word1) && counts.get(word1).containsKey(word2)) {
            //sSystem.out.print(counts.get(word1).get(word2));
            return counts.get(word1).get(word2);
        }
        return 0.0;
    }

    public double unigramCount(String word)
    {
        if (unigramCounts.containsKey(word)) {
            return unigramCounts.get(word);
        }
        return 0.0;
    }



    public double goodTuringSmoothedProbability(String word1, String word2)
    {
       // System.out.println(word1+word2);
        if (!goodTuringCountsAvailable) {
            System.out.println("Making good turing counts...");
            makeGoodTuringCounts();
            System.out.println("Done making good turing counts.");
        }

        // If this bigram has occurred, return good turing probability
        double gtcount = count(word1, word2);
        if (gtcount > 0.0) {
            if (!word1.equals(":S"))
                bigram += (word1 + " " + word2 + " " + gtcount / unigramCount(word1) + "\n");


            return gtcount / unigramCount(word1);
        }
        // Otherwise, return N1/N as per book (page 101?)
        if (!word1.equals(":S"))
            bigram += (word1 + " " + word2 + " " + numberOfBigramsWithCount.get(1.0) / numTrainingBigrams + "\n");


        return numberOfBigramsWithCount.get(1.0) / numTrainingBigrams;
    }

    public void makeGoodTuringCounts()
    {
        // Generate good turing counts
        for (String word1 : counts.keySet()) {

            HashMap<String, Double> innerMap = counts.get(word1);
            double unigramCount = 0;
            for (String word2 : innerMap.keySet()) {
                double count = innerMap.get(word2);
                if (!numberOfBigramsWithCount.containsKey(count+1)) {
                    numberOfBigramsWithCount.put(count+1, 0.0);
                }
                // c* = (c+1) * N(c+1) / N(c)
                double newCount = (count + 1)*(numberOfBigramsWithCount.get(count+1.0))/(numberOfBigramsWithCount.get(count));

                innerMap.put(word2, newCount);
                unigramCount += newCount;
               // System.out.println(word1+" "+word2);
            }

            unigramCounts.put(word1, unigramCount);
        }
        goodTuringCountsAvailable = true;
    }



    public double perplexity(Set<String> testSamples) {
        float product = 1;
        int wordCount = 0;
        Stack<Double> products = new Stack<Double>();
        String regexp = "('?\\S+|\\S)";
        Pattern pattern = Pattern.compile(regexp);

        // counting number of words in test set
        for (String sample : testSamples) {
            Matcher matcher = pattern.matcher(sample);
            String previousWord = START;
            while (matcher.find()) {
                String match = matcher.group();
               //System.out.println(previousWord+" "+match+" "+"="+goodTuringSmoothedProbability(previousWord, match));
                products.push(goodTuringSmoothedProbability(previousWord, match));
                wordCount++;

                // Update previousWord
                previousWord = match;
            }
        }

        // computing the necessary exponent
        double power = 1.0 / wordCount;

        // computing perplexity based on probabilities
        while (!products.empty()) {
            product *= Math.pow(products.pop(), power);
        }
        return 1 / product;
    }
    public String get_bigram(){
        return bigram;

}
}

