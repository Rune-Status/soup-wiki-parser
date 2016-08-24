package itemdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemDefs {

	public static void run() throws IOException {
		boolean test = true;

		File file = new File("data/itemdefs-dump.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		line = br.readLine();//skip first line because comment

		while ((line = br.readLine()) != null) {

			String[] parts = line.split(":");
			int id = Integer.parseInt(parts[0]);
			String name = parts[1];
			boolean noted = parts[2].equals("true");
			boolean noteable = parts[3].equals("true");
			boolean stackable = parts[4].equals("true");
			int parentId = Integer.parseInt(parts[5]);
			int notedId = Integer.parseInt(parts[6]);
			boolean members = parts[7].equals("true");
			int storePrice = Integer.parseInt(parts[8]);

			if (name.equals("null") || name.equals("Null")) {
				continue;
			}

			System.out.printf("Id: %d, Name: %s\n", id, name);

			ItemDef def = new ItemDef(id, name);
			def.noted = noted;
			def.noteable = noteable;
			def.stackable = stackable;
			def.parentId = parentId;
			def.notedId = notedId;
			def.members = members;
			def.specialStorePrice = storePrice;
			def.generalStorePrice = storePrice;

			if (test) {
				def.name = "Abyssal whip";
			}

			try {
				grabItem(def);
			} catch (HttpStatusException e) {
				continue;
			}

			if (test) {
				break;
			}
		}
		br.close();
	}

	private static void grabItem(ItemDef def) throws IOException {
		Document doc = Jsoup.connect("http://2007.runescape.wikia.com/wiki/" + def.name).get();

		/*
		 * Elements elements = doc.select("th"); for (int i = 0 ; i <
		 * elements.size(); i ++) { Element ele = elements.get(i);
		 * System.out.println(i + " -> " + ele); }
		 */

		Element examineElement = doc.select("td[style*=padding:3px 7px 3px 7px; line-height:140%; text-align:center;]").first();
		String examine = examineElement.text();
		System.out.printf("Examine: %s\n", examine);

		Element tradeElement = doc.select("a[href*=/wiki/Tradeable]").first();
		boolean tradeable = tradeElement.parent().parent().select("td").text().equalsIgnoreCase("Yes");
		System.out.printf("Tradeable: %b\n", tradeable);

		Element equipElement = doc.select("a[href*=/wiki/Equipment]").first();
		boolean equippable = equipElement.parent().parent().select("td").text().equalsIgnoreCase("Yes");
		System.out.printf("Equippable: %b\n", equippable);

		Element highAlchElement = doc.select("a[href*=/wiki/High_Level_Alchemy]").first();
		String highAlchValue = highAlchElement.parent().parent().select("td").text();
		if (highAlchValue.contains("coins")) {
			highAlchValue = highAlchValue.substring(0, highAlchValue.indexOf("coins")).replace(",", "").trim();
		}
		System.out.printf("highAlchValue: %s\n", highAlchValue);

		Element lowAlchElement = doc.select("a[href*=/wiki/Low_Level_Alchemy]").first();
		String lowAlchValue = lowAlchElement.parent().parent().select("td").text();
		if (lowAlchValue.contains("coins")) {
			lowAlchValue = lowAlchValue.substring(0, lowAlchValue.indexOf("coins")).replace(",", "").trim();
		}
		System.out.printf("lowAlchValue: %s\n", lowAlchValue);

		Element weightElement = doc.select("a[href*=/wiki/Weight]").first();
		String weightValue = weightElement.parent().parent().select("td").text();
		if (weightValue.contains("kg")) {
			weightValue = weightValue.substring(0, weightValue.indexOf("kg")).replace(",", "").trim();
		}
		System.out.printf("weightValue: %s\n", weightValue);

		boolean twoHanded = def.name.endsWith("2h sword") || def.name.endsWith("bow") || def.name.endsWith("godsword") || def.name.endsWith("halberd");
		System.out.printf("twohanded: %b", twoHanded);

		boolean plateBody = def.name.endsWith("platebody");
		System.out.printf("platebody: %b", plateBody);

		boolean fullhelm = def.name.endsWith("fullhelm");
		System.out.printf("fullhelm: %b", fullhelm);

		// Todo correct slots
		int slot = -1;
		if (equippable) {
			Element slotElement = doc.select("th[style*=text-align: center; width: 35px; font-size: 88%;]").first();
			System.out.println(slotElement.select("a").attr("title"));
		}
		System.out.printf("Slot: %s\n", slot);

		List<Integer> bonusesList = new ArrayList<Integer>();

		Elements bonuses = doc.select("td[style*=text-align: center; width: 35px;");
		for (int i = 0; i < bonuses.size(); i++) {
			Element bonus = bonuses.get(i);

			String actualBonus = bonus.toString();
			actualBonus = actualBonus.substring(actualBonus.indexOf("\">") + 2, actualBonus.indexOf("</td>"));
			actualBonus = actualBonus.replace("+", "").trim();

			System.out.printf("i: %d, bonus: %s\n", i, actualBonus);
			bonusesList.add(Integer.parseInt(actualBonus));
		}

		bonuses = doc.select("td[style*=text-align: center; width: 30px;");
		for (int i = 0; i < bonuses.size(); i++) {
			Element bonus = bonuses.get(i);

			String actualBonus = bonus.toString();
			actualBonus = actualBonus.substring(actualBonus.indexOf("\">") + 2, actualBonus.indexOf("</td>"));
			actualBonus = actualBonus.replace("+", "").replace("%", "").trim();

			System.out.printf("i: %d, bonus: %s\n", i + 10, actualBonus);
			bonusesList.add(Integer.parseInt(actualBonus));
		}

		int[] bonusesArray = new int[14];
		for (int i = 0; i < 14; i++) {
			bonusesArray[i] = bonusesList.get(i);
		}
		
		def.examine = examine;
		def.equipmentType = slot; // TODO
		def.highAlchValue = Integer.parseInt(highAlchValue.replace("\u00A0", ""));
		def.lowAlchValue = Integer.parseInt(lowAlchValue.replace("\u00A0", ""));
		def.weight = Double.parseDouble(weightValue.replace("\u00A0", ""));
		def.bonuses = bonusesArray;
		def.twoHanded = twoHanded;
		def.platebody = plateBody;
		def.fullHelm = fullhelm;
		def.tradeable = tradeable;
	}

}
