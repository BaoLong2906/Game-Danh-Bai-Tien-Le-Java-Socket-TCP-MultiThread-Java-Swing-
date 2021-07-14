package ModelCard;

import java.io.Serializable;

public class Card implements Serializable, Cloneable {
	public int index;
	public String house;
	public String point;
	public String url;
	
	public Card(int index, String house, String point, String url) {
		super();
		this.index = index;
		this.house = house;
		this.point = point;
		this.url = url;
	}
	
	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		if (object instanceof Card) {
			Card anotherCard = (Card) object;
			if (this.index == anotherCard.getIndex() && this.house.equals(anotherCard.getHouse()) &&
				this.point.equals(anotherCard.getPoint()) && this.url.equals(anotherCard.getUrl()))  {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
