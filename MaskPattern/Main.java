import java.io.Console;
public class Main {
	public static String GenerateStars(int len)
    {
        String stars = "";
        for (int i = 0; i < len; i++)
        {
            stars += '*';
        }
        return stars;
    }
	private static String removeLastChars(String str, int n) {
        return str.substring(0,str.length()-n);
    }
	public static void main(String[] args){
		String text = "Yesterday, I took my dog for a walk.\n It was crazy! My dog wanted only food.";
        String[] words = { "yesterday", "Dog", "food", "walk" }, result;

        for (String word : words) {
			result = text.split("(?i)"+word);
            text = "";
            for(String part : result)
            {
                text += part + GenerateStars(word.length());
            }
            text = removeLastChars(text, word.length());
		}
        System.out.println(text);
	}
}
