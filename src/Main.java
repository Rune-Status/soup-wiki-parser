import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import itemdefs.ItemDefs;

public class Main {

	public static void main(String[] args) throws IOException {
		ItemDefs.run();
	}

	private static void npcDrops() throws IOException {
		Document doc = Jsoup.connect("http://2007.runescape.wikia.com/wiki/General_Graardor").get();
		Elements tr = doc.select("tr");
		for (Element ele : tr) {
			System.out.println(ele.text().replace(" ", ":"));
		}
	}
}
