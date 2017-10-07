import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.io.*;

/*
 * General case n-gram for any n
 */
public class Ngram
{
    public static Set<String> samples; // Set of sample sentences to train from

    public int n; // (as in n-gram)
    public NgramCounter ngc; // The data structure for holding n-gram counts

    // For add-one smoothing
    public Set<String> wordSet; // Used to find the vocabulary
    public double vocabSize; // Size of the vocabulary
    public  String bigram_list="bigram.txt";
    public  String trigram_list="tigram.txt";
    public String content="";

    // For Good Turing Smoothing
    public double numTrainingNgrams; // The size of the training set (# non-distinct words)
    public HashMap<Double, Double> numberOfNgramsWithCount; // The number of ngrams that occur x times
    public boolean goodTuringCountsAvailable = false; // True when good turing counts are available

    public final String START = ":T"; // The sentence start symbol

    public static void main(String[] args) throws  Exception
    {


//

//        data_array apple=new data_array("src/a-fool-such-as-i-lyrics.txt");
//
//
//
//        HashSet<String> set= apple.full_data();

    }

    public Ngram(HashSet<String> samples, int n)
    {
        this.samples = samples;
        this.n = n;
        this.numberOfNgramsWithCount = new HashMap<Double, Double>();
        this.ngc = new NgramCounter(n, numberOfNgramsWithCount);

        this.wordSet = new HashSet<String>();

        this.numTrainingNgrams = 0;
    }

    public void train()
    {
        String regexp = "('?\\S+|\\S)";
        Pattern pattern = Pattern.compile(regexp);
        for (String sample : samples) {
            // Create list of words in the sample sentence & fill it
            ArrayList<String> sampleWords = new ArrayList<String>();
            Matcher matcher = pattern.matcher(sample);
            while (matcher.find()) {
                String match = matcher.group();

                sampleWords.add(match);
                //System.out.print(sampleWords);
                // Add to vocab for +1 smoothing
                wordSet.add(match);
            }

            // Add each group of n words to the n-gram counter, e.g., ...
            // [:S :S :S w1] w2 w3 w4 w5 w6
            // :S [:S :S w1 w2] w3 w4 w5 w6
            // :S :S [:S w1 w2 w3] w4 w5 w6
            // :S :S :S [w1 w2 w3 w4] w5 w6
            // :S :S :S w1 [w2 w3 w4 w5] w6
            // :S :S :S w1 w2 [w3 w4 w5 w6]
            String[] nWords = new String[n];
            for (int i = 0; i < n; i++) {
                nWords[i] = START;
            }
            for (String word : sampleWords) {
                for (int i = 0; i < n-1; i++) {
                    nWords[i] = nWords[i+1];
                }
                nWords[n-1] = word;

                // Add to the size of the training set for gt-smoothing
                numTrainingNgrams += 1;

                // Insert the words into the counter and receive count for this ngram
                double countForNgram = ngc.insert(nWords);

                // Decrement the number of ngrams with old countForNgram for gt-smoothing
                if (countForNgram != 1.0) {
                    numberOfNgramsWithCount.put(countForNgram-1,
                            numberOfNgramsWithCount.get(countForNgram-1) - 1.0);
                }
                // Increment the number of ngrams with the new countForNgram for gt-smoothing
                if (!numberOfNgramsWithCount.containsKey(countForNgram)) {
                    numberOfNgramsWithCount.put(countForNgram, 1.0);
                } else {
                    numberOfNgramsWithCount.put(countForNgram,
                            numberOfNgramsWithCount.get(countForNgram) + 1.0);
                }
            }
        }

        // Set the vocab size so we don't have to call wordSet.size() more than once
        vocabSize = wordSet.size();
    }



    public double goodTuringSmoothedProbability(String[] words)
    {
        if (!goodTuringCountsAvailable) {
            System.out.println("Making good turing counts...");
            makeGoodTuringCounts();
            System.out.println("Done making good turing counts.");
        }

        // If this ngram has occurred, return good turing probability
        double gtcount = ngc.gtcount(words);
        if (gtcount > 0) {

            return gtcount / ngc.level1GTCount(words);
        }
        // Otherwise, return N1/N as per book (page 101?)

        return numberOfNgramsWithCount.get(1.0)/numTrainingNgrams;
    }

    public void makeGoodTuringCounts()
    {
        // Generate good turing counts in the NgramCounter
        ngc.makeGoodTuringCounts();
        goodTuringCountsAvailable = true;
    }



    public double perplexity(Set<String> testSamples)
    {
        int wordCount = 0; // size of the test set
        Stack<Double> probabilities = new Stack<Double>(); // collection of probabilities to multiply

        String regexp = "('?\\S+|\\S)";
        Pattern pattern = Pattern.compile(regexp);
        String[] nWords = new String[n];
        for (String testSample : testSamples) {
            Matcher matcher = pattern.matcher(testSample);

            for (int i = 0; i < n; i++) {
                nWords[i] = START;
            }

            while (matcher.find()) {
                String match = matcher.group();
                // For each match, nWords is the ngram ending in match
                for (int i = 0; i < n-1; i++) {
                    nWords[i] = nWords[i+1];
                }
                nWords[n-1] = match;
                Double probability=goodTuringSmoothedProbability(nWords);
                String bigram=(nWords[n-3]+" "+nWords[n-2]+" "+nWords[n-1]+" "+Double.toString(probability));
                if (!bigram.contains(":T"))
                 content+=bigram+"\n";


                // Find the probability of the n-gram
                probabilities.push(probability);
                wordCount++;
            }
            //write_file("end.txt",content);
            //System.out.println(content);
           // content="";
        }

        // perplexity = ((P1 * P2 * ... * Pn) ^ (1/n)) ^ -1
        //            = (P1^(1/n) * P2^(1/n) * ... * Pn^(1/n)) ^ -1
        double product = 1;

        double power = 1.0/wordCount;
        while (!probabilities.empty()) {
            product *= Math.pow(probabilities.pop(), power);
        }
        double perplexity = 1 / product;
        return perplexity;
    }


public String get_trigram(){
        return content;
}

}