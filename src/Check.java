import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Check {

	private final static double REPEAT_RATE = 0.6f;
	// you can add your data-structure here
	private static Map<Character, Integer> charMap = null;
	private static int[][] charLocation = null;
	private static int[] eachDocSize = null;
	private static int docLength;

	// you must rewrite this function
	public void init(Vector<String> docList) {
		charMap = new HashMap<Character, Integer>();
		Map<Character, List<Integer>> standarChars = new HashMap<Character, List<Integer>>();
		docLength = docList.size() / 4;
		eachDocSize = new int[docLength];

		int keyCount = 0;
		for (int i = 0; i < docLength; i++) {
			String doc = docList.get(i);
			if (doc != null && doc.length() > 0) {
				Set<Character> docFeatureSet = getDocFeature(doc);//所有关键字集合，不含重复
				eachDocSize[i] = (int) (docFeatureSet.size() * REPEAT_RATE + 1);
				Iterator<Character> iterator = docFeatureSet.iterator();
				while (iterator.hasNext()) {
					Character key = iterator.next();
					if (standarChars.containsKey(key)) {
						List<Integer> values = standarChars.get(key);
						values.add(i);
					} else {
						List<Integer> values = new ArrayList<Integer>();
						values.add(i);
						standarChars.put(key, values);//关键字-所有出现的行位置
						charMap.put(key, keyCount);
						keyCount++;
					}
				}
			}
		}
		charLocation = new int[standarChars.size()][];
		for (Iterator<Character> iterator = standarChars.keySet().iterator(); iterator
				.hasNext();) {
			Character key = (Character) iterator.next();
			List<Integer> keyLocations = standarChars.get(key);
			int keyvalue = charMap.get(key);

			charLocation[keyvalue] = new int[keyLocations.size()];
			for (int i = 0; i < keyLocations.size(); i++) {
				charLocation[keyvalue][i] = keyLocations.get(i);
			}
		}
		// System.out.println("init complete");
		// for(int i = 0; i < charLocation.length; i++){
		// System.out.println(charLocation[i].length);
		// for(int j = 0; j < charLocation[i].length; j++){
		// System.out.print(charLocation[i][j] + " ");
		// }
		// System.out.println();
		// }
	}

	// my function
	private Set<Character> getDocFeature(String doc) {
		Set<Character> docFeatureSet = new HashSet<Character>();
		int docSize = doc.length();
		for (int i = 0; i < docSize; i++) {
			Character key = doc.charAt(i);
			docFeatureSet.add(key);
		}
		return docFeatureSet;
	}

	static long sumtim = 0;

	// you must rewrite this function
	// checking
	// IN tiezi : doc string
	// OUT : 1--->hit doc
	// 0--->miss doc
	public int check(char[] info, int infoLen) {
		// compare all items
		Set<Character> tmpDocFeatureSet = new HashSet<Character>();

		for (int i = 0; i < infoLen; i++) {
			if (charMap.containsKey(info[i])) {
				tmpDocFeatureSet.add(info[i]);
			}
		}
		int[] tempSize = new int[docLength];

		Iterator<Character> iterator = tmpDocFeatureSet.iterator();
		while (iterator.hasNext()) {
			Character key = iterator.next();
			int values = charMap.get(key);
			int keyLength = charLocation[values].length;
			for (int j = 0; j < keyLength; j++) {
				int sit = charLocation[values][j];
				tempSize[sit]++;
				// if(tempSize[sit] == eachDocSize[sit]){
				// return 1;
				// }
			}
		}
		for (int i = 0; i < docLength; i++) {
			if (tempSize[i] >= eachDocSize[i]) {
				return 1;
			}
		}
		return 0;
	}
}
