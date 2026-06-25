package com.onewhohears.dscombat.data.vehicle;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VehicleDecalManager {

    public static final int MAX_DECALS = 16;

    public final EntityVehicle parent;
    private final List<DecalData> decals = new ArrayList<>();
    private boolean changed = false;

    public VehicleDecalManager(EntityVehicle parent) {
        this.parent = parent;
    }

    public List<DecalData> getDecals() {
        return decals;
    }

    public boolean isChanged() {
        return changed;
    }

    public void resetChanged() {
        changed = false;
    }

    /** Add a new decal. Returns false if max reached. */
    public boolean addDecal(DecalData decal) {
        if (decals.size() >= MAX_DECALS) return false;
        decals.add(decal);
        changed = true;
        return true;
    }

    /** Remove decal by id. Returns true if found and removed. */
    public boolean removeDecal(String id) {
        boolean removed = decals.removeIf(d -> d.id.equals(id));
        if (removed) changed = true;
        return removed;
    }

    /** Update existing decal by id. Returns true if found. */
    public boolean updateDecal(DecalData updated) {
        for (int i = 0; i < decals.size(); i++) {
            if (decals.get(i).id.equals(updated.id)) {
                decals.set(i, updated);
                changed = true;
                return true;
            }
        }
        return false;
    }

    public void read(CompoundTag nbt) {
        decals.clear();
        if (!nbt.contains("decals")) return;
        ListTag list = nbt.getList("decals", 10);
        for (int i = 0; i < list.size(); i++) {
            decals.add(DecalData.fromNbt(list.getCompound(i)));
        }
        changed = false;
    }

    public void write(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (DecalData d : decals) list.add(d.toNbt());
        nbt.put("decals", list);
    }

    public void read(FriendlyByteBuf buf) {
        decals.clear();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            decals.add(DecalData.fromBuf(buf));
        }
        changed = false;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(decals.size());
        for (DecalData d : decals) d.toBuf(buf);
    }

    // -------------------------------------------------------------------------

    public static class DecalData {
        public String id;
        public String text;
        public float x, y, z;         // local position relative to vehicle center
        public float rotX, rotY, rotZ; // euler angles in degrees
        public float scale;
        public int color;              // ARGB

        public DecalData(String text, float x, float y, float z,
                         float rotX, float rotY, float rotZ, float scale, int color) {
            this.id = UUID.randomUUID().toString();
            this.text = text;
            this.x = x; this.y = y; this.z = z;
            this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
            this.scale = scale;
            this.color = color;
        }

        public DecalData(String id, String text, float x, float y, float z,
                         float rotX, float rotY, float rotZ, float scale, int color) {
            this.id = id;
            this.text = text;
            this.x = x; this.y = y; this.z = z;
            this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
            this.scale = scale;
            this.color = color;
        }

        private DecalData() {}

        public CompoundTag toNbt() {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", id);
            tag.putString("text", text);
            tag.putFloat("x", x); tag.putFloat("y", y); tag.putFloat("z", z);
            tag.putFloat("rx", rotX); tag.putFloat("ry", rotY); tag.putFloat("rz", rotZ);
            tag.putFloat("scale", scale);
            tag.putInt("color", color);
            return tag;
        }

        public static DecalData fromNbt(CompoundTag tag) {
            DecalData d = new DecalData();
            d.id = tag.getString("id");
            d.text = tag.getString("text");
            d.x = tag.getFloat("x"); d.y = tag.getFloat("y"); d.z = tag.getFloat("z");
            d.rotX = tag.getFloat("rx"); d.rotY = tag.getFloat("ry"); d.rotZ = tag.getFloat("rz");
            d.scale = tag.getFloat("scale");
            d.color = tag.getInt("color");
            return d;
        }

        public void toBuf(FriendlyByteBuf buf) {
            buf.writeUtf(id);
            buf.writeUtf(text);
            buf.writeFloat(x); buf.writeFloat(y); buf.writeFloat(z);
            buf.writeFloat(rotX); buf.writeFloat(rotY); buf.writeFloat(rotZ);
            buf.writeFloat(scale);
            buf.writeInt(color);
        }

        public static DecalData fromBuf(FriendlyByteBuf buf) {
            DecalData d = new DecalData();
            d.id = buf.readUtf();
            d.text = buf.readUtf();
            d.x = buf.readFloat(); d.y = buf.readFloat(); d.z = buf.readFloat();
            d.rotX = buf.readFloat(); d.rotY = buf.readFloat(); d.rotZ = buf.readFloat();
            d.scale = buf.readFloat();
            d.color = buf.readInt();
            return d;
        }
    }
}
