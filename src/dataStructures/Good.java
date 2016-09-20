package dataStructures;

import analyzer.GoodTypeIdentificator;

public class Good {
	static GoodTypeIdentificator reconizer;
	String merchant="";
	String title;
	String description="";
	float price;
	TypeOfGoods type;
	
	public Good(String merch, String titl,String desc,float price) {
		if (merch!=null)
			merchant = merch;
		title = titl;
		if (desc!=null) {
			description = desc;
		}
		this.price = price;
	}
	
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public TypeOfGoods getType() {
		if (type==null) {
			type=reconizer.reconizeType(this);
		}
		return type;
	}
	
	
	
}
