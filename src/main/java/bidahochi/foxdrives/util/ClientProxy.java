package bidahochi.foxdrives.util;


import bidahochi.foxdrives.entities.EntityCar;
import bidahochi.foxdrives.entities.EntityCarChest;
import bidahochi.foxdrives.entities.EntitySeat;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final RenderCar transportRenderer = new RenderCar();
    /**the keybind for opening the inventory*/
    public static KeyBinding KeyInventory = new KeyBinding("Open car GUI",  Keyboard.KEY_R, "Fox Drives");
    public static KeyBinding KeyBrake = new KeyBinding("Brake",  Keyboard.KEY_B, "Fox Drives");

    @Override
    public Object getEntityRender(){return transportRenderer;}

    @Override
    public boolean isClient(){return true;}

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("Open client");
        if(player.worldObj.getEntityByID(ID) instanceof EntityCarChest){
            System.out.println("Open client");
            return new GuiCarInventory(player.inventory, (EntityCarChest) player.worldObj.getEntityByID(ID));
        }
        return null;
    }

    public static final net.minecraft.client.renderer.entity.RenderPlayer playerRender = new net.minecraft.client.renderer.entity.RenderPlayer(){
        EntityCar car;
        @Override
        public void doRender(net.minecraft.client.entity.AbstractClientPlayer player, double x, double y, double z, float f0, float f1){
			if(player.ridingEntity instanceof EntitySeat || player.ridingEntity instanceof EntityCar){
				car = (EntityCar)(player.ridingEntity instanceof EntityCar ? player.ridingEntity : ((EntitySeat)player.ridingEntity).car);
				if(car != null){
					GL11.glPushMatrix();
                    float scale = car.type().rider_scale;
					scale = player.height * scale / player.height;
					GL11.glTranslated(x, y, z);
					GL11.glScalef(scale, scale, scale);
					GL11.glTranslated(-x, -y, -z);
					super.doRender(player, x, y, z, f0, f1);
					GL11.glPopMatrix();
					return;
				}

			}
			super.doRender(player, x, y, z, f0, f1);
        }
    };

    @Override
    public void registerCarRenderer(Class<? extends EntityCar> clazz){
        RenderingRegistry.registerEntityRenderingHandler(clazz, transportRenderer);
    }

    @Override
    public void registerPlayerScaler(){
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, playerRender);
        RenderingRegistry.registerEntityRenderingHandler(EntitySeat.class, new RenderSeat());
    }

}
