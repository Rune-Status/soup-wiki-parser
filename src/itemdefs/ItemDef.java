package itemdefs;

import java.io.BufferedWriter;

public class ItemDef {
	public int id;
	public String name;
	public String examine;
	public int equipmentType = -1;
	public boolean noted = false;
	public boolean noteable = false;
	public boolean stackable = false;
	public int parentId = -1;
	public int notedId = -1;
	public boolean members = true;
	public int specialStorePrice = 0;
	public int generalStorePrice = 0;
	public int highAlchValue = 0;
	public int lowAlchValue = 0;
	public double weight = 0;
	public int[] bonuses;
	public boolean twoHanded = false;
	public boolean platebody = false;
	public boolean fullHelm = false;
	public boolean tradeable = false;

	public ItemDef(int id, String name, String examine, int equipmentType, boolean noted, boolean noteable, boolean stackable, int parentId, int notedId, boolean members, int specialStorePrice, int generalStorePrice, int highAlchValue, int lowAlchValue, double weight, int[] bonuses, boolean twoHanded, boolean platebody, boolean fullHelm, boolean tradeable) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.equipmentType = equipmentType;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.members = members;
		this.specialStorePrice = specialStorePrice;
		this.generalStorePrice = generalStorePrice;
		this.highAlchValue = highAlchValue;
		this.lowAlchValue = lowAlchValue;
		this.weight = weight;
		this.bonuses = bonuses;
		this.twoHanded = twoHanded;
		this.platebody = platebody;
		this.fullHelm = fullHelm;
		this.tradeable = tradeable;
	}
	
	public ItemDef(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void write(BufferedWriter writer) {
		//write
	}
}
