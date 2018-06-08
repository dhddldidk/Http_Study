package kr.or.dgit.it.http_study.parser;

public class Item {
    private String itemName;
    private String makerName;
    private int itemPrice;

    public Item() {
    }

    public Item(String itemName, String makerName, int itemPrice) {
        this.itemName = itemName;
        this.makerName = makerName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getMakerName() {
        return makerName;
    }
    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }
    public int getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", makerName='" + makerName + '\'' +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
