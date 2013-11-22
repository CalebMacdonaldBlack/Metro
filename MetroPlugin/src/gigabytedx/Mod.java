package gigabytedx;

public class Mod {

	int		x, z;
	String	name;

	public Mod(int x, int z, String name) {

		this.x = x;
		this.z = z;
		this.name = name;
	}

	public Mod() {

	}

	public int getX() {

		return x;
	}

	public void setX(int x) {

		this.x = x;
	}

	public int getZ() {

		return z;
	}

	public void setZ(int z) {

		this.z = z;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

}
