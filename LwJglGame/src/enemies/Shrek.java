package enemies;

import mesh.MeshInstance;

import org.lwjgl.util.vector.Vector3f;

import player.Player;

public class Shrek extends Enemy{

	public Shrek(Vector3f position, Player player, MeshInstance mInstance) {
		super(position, player,mInstance);
		// TODO Auto-generated constructor stub
		headHeight=4;
	}

}
