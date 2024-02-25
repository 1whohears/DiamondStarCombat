/**
 * code stolen from here
 * https://github.com/endorh/lazulib/blob/1.19/src/main/java/endorh/util/nbt/JsonToNBTUtil.java
 */
package com.onewhohears.dscombat.util;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.intellij.lang.annotations.Language;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import net.minecraft.Util;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class JsonToNBTUtil {
	
	public static CompoundTag getTagFromJson(JsonElement element) {
		if (!element.isJsonObject())
			throw new JsonSyntaxException("Tag must be an object");
		return readObject(element.getAsJsonObject());
	}
	
	public static CompoundTag getTagFromJson(String json) {
		return getTagFromJson(json, false);
	}
	
	@SuppressWarnings("deprecation")
	public static CompoundTag getTagFromJson(String json, boolean lenient) {
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(lenient);
		return getTagFromJson(new JsonParser().parse(reader));
	}
	
	@SuppressWarnings("deprecation")
	public static CompoundTag getTagFromJson(Reader json) {
		return getTagFromJson(new JsonParser().parse(json));
	}
	
	@SuppressWarnings("deprecation")
	public static CompoundTag getTagFromJson(JsonReader json) {
		return getTagFromJson(new JsonParser().parse(json));
	}
	
	private static Tag readElement(JsonElement elem) {
		if (elem.isJsonNull()) {
			throw new JsonSyntaxException("Null value is not allowed in NBT JSON");
		} else if (elem.isJsonObject()) {
			return readObject(elem.getAsJsonObject());
		} else if (elem.isJsonArray()) {
			return readArray(elem.getAsJsonArray());
		} else if (elem.isJsonPrimitive()) {
			return readValue(elem.getAsJsonPrimitive());
		} else {
			throw new IllegalArgumentException("Unknown JSON type: " + elem);
		}
	}
	
	private static CompoundTag readObject(JsonObject obj) {
		final CompoundTag nbt = new CompoundTag();
		for (Entry<String, JsonElement> child : obj.entrySet())
			nbt.put(child.getKey(), readElement(child.getValue()));
		return nbt;
	}
	
	private static Tag readArray(JsonArray array) {
		int n = array.size();
		if (n < 1)
			//throw new JsonSyntaxException("Empty NBT array must specify its type");
			return new ListTag();
		JsonElement first = array.get(0);
		final String differentTypes = "All elements in array must be of the same type";
		if (first.isJsonPrimitive()) {
			JsonPrimitive prim = first.getAsJsonPrimitive();
			if (prim.isString()) {
				final String type = prim.getAsString();
				if ("B".equals(type) || "I".equals(type) || "L".equals(type)) {
					List<Number> list = new ArrayList<>();
					for (int i = 1; i < n; i++) {
						try {
							JsonPrimitive p = array.get(i).getAsJsonPrimitive();
							if (p.isBoolean()) {
								list.add(p.getAsBoolean() ? 1 : 0);
							} else if (p.isNumber()) {
								list.add(p.getAsNumber());
							} else throw new JsonSyntaxException(differentTypes);
						} catch (IllegalStateException e) {
							throw new JsonSyntaxException(differentTypes);
						}
					}
					return switch (type) {
						case "B" -> new ByteArrayTag(
						  list.stream().map(Number::byteValue).collect(Collectors.toList()));
						case "I" -> new IntArrayTag(
						  list.stream().map(Number::intValue).collect(Collectors.toList()));
						case "L" -> new LongArrayTag(
						  list.stream().map(Number::longValue).collect(Collectors.toList()));
						default -> throw new IllegalStateException();
					};
				}
			}
		}
		List<Tag> ls = new ArrayList<>();
		ListTag list = new ListTag();
		for (JsonElement elem : array)
			ls.add(readElement(elem));
		try {
			if (ls.isEmpty())
				return list;
			if (ls.get(0) instanceof NumericTag) {
				Class<?> cls = null;
				for (Tag elem : ls) {
					if (!(elem instanceof NumericTag))
						throw new JsonSyntaxException(differentTypes);
					cls = combineNumericTypes(cls, elem.getClass());
				}
				final Function<Number, NumericTag> con = numericConstructors.get(cls);
				for (Tag elem : ls)
					list.add(con.apply(((NumericTag) elem).getAsNumber()));
			} else list.addAll(ls);
		} catch (UnsupportedOperationException e) {
			throw new JsonSyntaxException(differentTypes);
		}
		return list;
	}
	
	protected static Class<?> combineNumericTypes(Class<?> a, Class<?> b) {
		if (a == FloatTag.class || a == DoubleTag.class || b == FloatTag.class || b == DoubleTag.class) {
			if (a == LongTag.class || a == DoubleTag.class || b == LongTag.class || b == DoubleTag.class)
				return DoubleTag.class;
			else return FloatTag.class;
		}
		if (a == LongTag.class || b == LongTag.class)
			return LongTag.class;
		else if (a == IntTag.class || b == IntTag.class)
			return IntTag.class;
		else if (a == ShortTag.class || b == ShortTag.class)
			return ShortTag.class;
		return ByteTag.class;
	}
	
	protected static final Map<Class<?>, Function<Number, NumericTag>> numericConstructors =
	  Util.make(new HashMap<>(), m -> {
		m.put(ByteTag.class, n -> ByteTag.valueOf(n.byteValue()));
		m.put(ShortTag.class, n -> ShortTag.valueOf(n.shortValue()));
		m.put(IntTag.class, n -> IntTag.valueOf(n.intValue()));
		m.put(LongTag.class, n -> LongTag.valueOf(n.longValue()));
		m.put(FloatTag.class, n -> FloatTag.valueOf(n.floatValue()));
		m.put(DoubleTag.class, n -> DoubleTag.valueOf(n.doubleValue()));
	});
	
	private static final Map<Pattern, Function<String, Tag>> PATTERN_MAP = new LinkedHashMap<>();
	static {
		pat("([-+]?(?:\\d+[.]?|\\d*[.]\\d+)(?:e[-+]?\\d+)?)d", Double::parseDouble, DoubleTag::valueOf);
		pat("([-+]?(?:\\d+[.]?|\\d*[.]\\d+)(?:e[-+]?\\d+)?)f", Float::parseFloat, FloatTag::valueOf);
		pat("([-+]?(?:0|[1-9]\\d*))b", Byte::parseByte, ByteTag::valueOf);
		pat("([-+]?(?:0|[1-9]\\d*))l", Long::parseLong, LongTag::valueOf);
		pat("([-+]?(?:0|[1-9]\\d*))s", Short::parseShort, ShortTag::valueOf);
		pat("([-+]?(?:0|[1-9]\\d*))i", Integer::parseInt, IntTag::valueOf);
		// pat("([-+]?(?:0|[1-9]\\d*))", Integer::parseInt, IntNBT::valueOf);
		// pat("([-+]?(?:\\d+[.]|\\d*[.]\\d+)(?:e[-+]?\\d+)?)", Double::parseDouble,
		// DoubleNBT::valueOf);
		pat("(false|true)", str -> "true".equalsIgnoreCase(str)? ByteTag.ONE : ByteTag.ZERO);
	}
	private static <T> void pat(
	  @Language("RegExp") String pattern, Function<String, T> parser, Function<T, Tag> caster
	) { pat(pattern, parser.andThen(caster)); }
	private static void pat(@Language("RegExp") String pattern, Function<String, Tag> parser) {
		PATTERN_MAP.put(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), parser);
	}
	
	private static Tag readValue(JsonPrimitive elem) {
		if (elem.isBoolean()) {
			return ByteTag.valueOf(elem.getAsBoolean());
		} else if (elem.isNumber()) {
			if (!elem.getAsString().contains(".")) {
				return IntTag.valueOf(elem.getAsInt());
			} else {
				return DoubleTag.valueOf(elem.getAsDouble());
			}
		} else if (elem.isString()) {
			String str = elem.getAsString();
			try {
				for (Pattern pattern : PATTERN_MAP.keySet()) {
					Matcher m = pattern.matcher(str);
					if (m.matches()) {
						return PATTERN_MAP.get(pattern).apply(m.group(1));
					}
				}
			} catch (NumberFormatException ignored) {}
			return str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"'
			       ? StringTag.valueOf(str.substring(1, str.length() - 1)) : StringTag.valueOf(str);
		} else {
			throw new IllegalStateException("Unknown JSON primitive: " + elem);
		}
	}
}
