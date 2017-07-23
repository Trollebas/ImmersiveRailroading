package cam72cam.immersiverailroading;

import net.minecraftforge.common.config.Config.Comment;

@net.minecraftforge.common.config.Config(modid=ImmersiveRailroading.MODID)
public class Config {
	@Comment({"A list of locomotives to load"})
	public static String[] locomotives = new String[] {"drg_class_01", "drg_class_80", "shay", "sd40", "sd70", "v60", "e69"};

	@Comment({"km/h to damage 1 heart on collision"})
	public static double entitySpeedDamage = 10;
	
	@Comment({"Self explanitory"})
	public static boolean trainsOnTheBrain = true;
}
