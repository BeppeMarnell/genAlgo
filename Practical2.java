import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 * 
 * @author Jo Stevens
 * @version 1.0, 14 Nov 2008
 * 
 * @author Alard Roebroeck
 * @version 1.1, 12 Dec 2012
 * 
 */

public class Practical2 {

	static final String TARGET = "HELLO WORLD";
	static double mutationRate = 0.01;

	static char[] alphabet = new char[27];
	static ArrayList<Individual> population;

	static Random generator = new Random(System.currentTimeMillis());

	//MAIN VOID THAT STARTS ALL
	public static void main(String[] args) {
		int popSize = 100;
		//generate the alphabet to use to generate the population
		genAlfabeth();
		//generate initial popolation
		genPop(popSize, TARGET.length());


		// What does your population look like?
		for (int i = 0; i < population.size(); i++) {
			System.out.println("n: " +i + " " +population.get(i).genoToPhenotype());
		}

		int iterations =0;
		while (!draw()){
			iterations++;
		}

		System.out.println("n of iterations: " + iterations);
	}

	//Method to attuate the genethic algorithm
	public static boolean draw(){

		//SELECTION
		int[][] sortedFitn = calcFitness(population).clone();
		//go out the loop when the fitness of the first is == Target.length
		if(sortedFitn[0][1]== TARGET.length()) return true;

		//REPRODUCTION
		reproduCe(sortedFitn);

		return false;
	}

	static public void reproduCe(int[][] sortedFitness){
		// SELECT PARENTS
		int totalFit = 0;
		ArrayList<int[]> selected = new ArrayList<>();

		for(int i=0; i<sortedFitness.length; i++){
			if(sortedFitness[i][1]>=1) {
				selected.add(new int[]{i, sortedFitness[i][1]});
				totalFit += sortedFitness[i][1];
			}
		}

		// 4 strings 1f= 2 2f= 5 3f=6 4f =1  tf = 14
		// 2/14 *100 = 14
		// 5/14 *100 = 36
		// 6/14 *100 = 43
		// 1/14 *100 = 7

		int[] tmp = new int[100];
		int pointer =0;
		for(int i=0; i<sortedFitness.length; i++){
			int quantity = Math.round(sortedFitness[i][1]/totalFit * 100);
			for(int j=0; j< quantity; j++)
				tmp[j + pointer] = i; // write the index in the tmp array
			pointer += quantity;
		}
		//now i select two random number between 0 and 99 in tmp
		//random.nextInt(max - min + 1) + min
		Random rnd = new Random();
		int first = rnd.nextInt(100);
		int second = rnd.nextInt(100);

		//CROSSOVER AND MUTATION
		String son = generateSon(population.get(first).chromosome,
				population.get(second).chromosome);
		//System.out.println("son " + son);

		population.add(new Individual(son.toCharArray()));
	}

	static public String generateSon(char[] f1, char[] f2){
		// crossover
		char[] son = new char[TARGET.length()];
		int middle = Math.round(TARGET.length()/2);
		for (int i=0; i< TARGET.length(); i++){
			if(i<=middle) son[i] = f1[i];
			else son[i] = f2[i];
		}

		// mutation
		if(Math.random() < 0.02){
			Random rnd = new Random();
			son[rnd.nextInt(son.length)] = genChars(1)[0];
		}

		StringBuilder builder = new StringBuilder();
		builder.append(son);
		return builder.toString();
	}

	//function to calculate the fitness
	static public int[][] calcFitness(ArrayList<Individual> population){
		int[][] fitness = new int[population.size()][2];

		//create the id array
		for(int i=0; i< fitness.length; i++){
			fitness[i][0] = i ;

			//calculate the fitness of the population array and put into fitness
			int c=0;
			for(int j=0; j<TARGET.length(); j++){
				if(population.get(i).getChromosome()[j] == TARGET.charAt(j))
					c++;
			}
			fitness[i][1] = c;
			System.out.println("c: " +c);
		}

		//sort by fitness while keeping the ids
		Arrays.sort(fitness, Comparator.comparingInt(arr -> arr[1]));

		/**
		 * Arrays.sort(temp, new Comparator<int[]>() {
		@Override
		public int compare(int[] o1, int[] o2) {
		return Integer.compare(o2[1], o1[1]);
		}
		});
		 */

		return fitness;
	}

	//FIRST STATE POP GENERATION / CHARACTER CREATION
	static public void genPop(int size, int targetSize){
		population = new ArrayList<>();

		// we initialize the population with random characters
		for (int i = 0; i < size; i++) {
			population.add(new Individual(genChars(targetSize)));
		}

	}

	static public char[] genChars(int targetSize){

		char[] tempChromosome = new char[targetSize];
		for (int j = 0; j < targetSize; j++) {
			tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; //choose a random letter in the alphabet
		}
		return tempChromosome;
	}

	static private void genAlfabeth(){
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';
	}
}
