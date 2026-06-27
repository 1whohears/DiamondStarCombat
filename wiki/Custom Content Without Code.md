This page is updated for v0.14.3. If anything is unclear please let me know in the discord so I can update this page!

# The JSON Preset System

Vehicles, weapons, radars, and parts use Minecraft's data system to load their stats. **I'd recommend getting familiar with how modifying/creating custom recipes works with Datapacks in Vanilla Minecraft before going any further.** Similar to how every recipe has a JSON file: every vehicle, weapon, and radar have their own JSON files that can be modified. I call these JSON files presets.

Presets store all the stats and information that all instances of that preset share. For example: all Alexis Planes use the `alexis_plane_empty.json` preset file to determine their max health, max speed, ext...I recommend exploring the [presets that come with the mod](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/vehicle) and get accustomed to how these json files are written.

## Datapack Basics

One can create preset json files and put them in a datapack to modify and create custom vehicles, weapons, and vehicle part items. Below will be detailed explanations of how to create each. (Here is an example content pack)
<details> 

<summary>Here is a rough map of the folder structure your Datapack could be:</summary>

```
ExamplePackRoot
/data
  /dscombat
    /vehicle
      +alexis_plane.json (modify default preset)
    /radars
      +ar1k.json (modify default preset)
    /weapons
      +20mm.json  (modify default preset)
  /examplepackid
    /vehicle
      +alexis_plane_custom.json
    /radars
      +custom_radar.json
    /weapons
      +custom_weapon.json
```
</details> 

## Preset Super Types

As seen in the datapack folder structure above, there are a few folders where presets are stored. This is so presets can be organized by what I call Super Types. Each Super Type has a list of Preset Types associated with it. For example presets in Super Type `vehicle` can be cars, planes, helicopters...ext and presets in Super Type `weapons` can be bullets, missiles, bombs, ext...

- Vehicle presets -> `vehicle` folder
- Weapon presets -> `weapons` folder
- Part Item presets -> `parts` folder
- Radar presets -> `radars` folder
- Stat Graph presets -> `stat_graph` folder

## Preset JSON File Structure

JSON parameters will be documented in the following format hence forth:

`parameter_name` | Value Type | **default_value** | *description*

Every preset file can have these parameters:

`presetId` | STRING | **JSON_FILE_NAME** | *MUST BE EQUAL TO JSON FILE NAME OR LEFT EMPTY (ASSUMES JSON NAME)*

`presetType` | STRING | **REQUIRED** | *Determines what kind of stats the preset should have.
Can determine if the preset is a plane or a boat for example. Scroll down to see each preset super type's available preset types.*
<details> 

<summary>Example generic preset file `example_preset.json`:</summary>

```
{
    "presetId": "example_preset",
    "presetType": "example_type"
}
```
</details> 

`displayName` | STRING | **preset.[`namespace`].[`presetId`]** | *Optionally set a custom translatable lang key.*

`copyId` | STRING | **OPTIONAL** | *Inherit all the stats from the preset with this id. Scroll down for more info.*

`priority` | INTEGER | **0** | *If another preset with the same `presetId` is found, the preset with the highest `priority` will be the one that is used.*

`sort_factor` | INTEGER | **0** | *Presets with lower sort factors are sorted before presets with higher sort factors. This is useful for the crafting workbenches if you want a preset to be listed before/after others.*

### Preset Inheritance

The `copyId` parameter allows a preset inherit all the stats from the preset with that id. This lets you make multiple presets with similar stats without copying and pasting the same file multiple times. One can also "override" parent stats in the child preset. Additionally, one can make the inheritance tree as long as they want.
<details>

<summary>Note the 3 example presets `dog.json`, `fast_dog.json`, and `lazy_dog.json`: </summary>

```
{
    "presetId": "dog",
    "presetType": "animal",
    "health": 100,
    "mass": 100,
    "speed": 4
}
{
    "presetId": "fast_dog",
    "presetType": "animal",
    "copyId": "dog",
    "mass": 60,
    "speed": 10
}
{
    "presetId": "lazy_dog",
    "presetType": "animal",
    "copyId": "fast_dog",
    "speed": 2
}
```

Notice that `presetId` and `presetType` still must be defined in all examples. Because `fast_dog` copies `dog`, both presets have a health stat of 100. However, `fast_dog` overrides the speed stat. Thus, `fast_dog` has a speed of 10, and `dog` has a speed of 4. `lazy_dog` has the same mass of `fast_dog`, but not the same speed.
</details> 

# Vehicles

Vehicle presets are the most complex presets to design as you'd expect. If your goal is to make a custom vehicle, you are going to need to create a vehicle preset within a datapack, and then create several assets and make sure they are all in the right folders. I will document every parameter within the json files, and every asset you will need to make your dream custom vehicle operational. This information will also be useful if you merely want to modify some of the vehicles that are in the base mod.

## Data

The following is an explanation of all the preset parameters that are unique to vehicles.

### Available Preset Types (`presetType`)

- `plane`
- `helicopter`
- `car`
- `boat`
- `submarine`

For vehicles the `presetType` determines the entity type that is used to summon the vehicle. Each vehicle entity has different physics and stats. For example boats run additional physics calculations for buoyancy that other entities don't.

### General Conventions

Preset Inheritance is used a lot in vehicles. Each vehicle has a base/root preset which is called `empty`. The health, armor, movement stats, available part slots, hitboxes, recipe ingredients, ext are all defined in the empty preset. But the empty preset has no engine, no fuel tanks, no weapons, ext. Then there is an `unarmed` preset which puts the `empty` preset id in `copyId` and adds engines and fuel tanks but no weapons. See `slots` for how this is done. This is typically the preset that gets crafted in the vehicle crafting workbench. Then there is the "default" preset which uses the `unarmed` preset id as its `copyId` and adds weapons. The unarmed and default presets all share the same stats, hitboxes, and sounds. But they have different weapon load outs.

### Full Parameter List

`is_craftable` | BOOLEAN | **false** | *This parameter lies and is only used by my automatic preset generator. It doesn't actually make a vehicle preset appear in the vehicle workbench. You need to add a recipe json file to the recipes data folder. See [example preset recipe that comes with the mod](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/recipes/workbench_vehicle_alexis_plane_unarmed.json)*

`item` | RESOURCE_LOCATION | **dscombat:vehicle** | *Only change this if you make a mod that adds a new item that extends the ItemVehicle class!*

`display_name_base` | STRING | **item.dscombat.`assetId`** | *Define a translation key that identifies this vehicle type without any preset desciptors. For example if `displayName` = Default Alexis Plane then `display_name_base` = Alexis Plane*

`landing_gear` | BOOLEAN | **false** | *If this vehicle uses landing gear like a plane or a helicopter, set this to true. That way when you spawn a new vehicle on the ground, its landing gear are already activated.*

`health` | NUMBER | **0** | *The initial health a vehicle spawns with. Should be equal to `max_health`. Yes you need to define `health` and `max_health` separately. Must be positive!*

`armor` | NUMBER | **0** | *The initial armor a vehicle spawns with. Should be equal to `base_armor`. Yes you need to define `armor` and `base_armor` separately. Must be positive!*

`stats` | JSON_OBJECT | **REQUIRED** | *Where a lot of general stats used by all vehicles are stored.*
- `assetId` | STRING | **`presetId`** | *The id that the client side uses to get the vehicle's assets. Should be the same name as the Vehicle Client json file, and the texture folder. If a vehicle client json file does not exist, then the vehicle model file's name should be `assetId`.*
- `max_health` | NUMBER | **10** | *The maximum health this vehicle can have. Must be positive!*
- `max_speed` | NUMBER | **0.1** | *The maximum horizontal speed in `meters/tick`. Must be positive!*
- `max_ground_speed` | NUMBER | **max_speed** | *The maximum speed the vehicle will travel while on the ground in `meters/tick`*
- `cruise_speed` | NUMBER | **max_speed** | *The max speed a vehicle can reach if the afterburner is OFF in `meters/tick`.
  `max_speed` is the speed a vehicle can reach if the afterburner is ON.*
- `use_horizontal_speed_scale` | BOOLEAN | **false** | *If `true`, all horizontal in game speeds will be 1/8th the speed parameters in this preset file.
  The 1/8th scale value will be configurable in the future. If you are making a custom fighter jet, set this to `true`, and make all speeds like `max_speed`
  and `cruise_speed` the real life speeds in `meters/tick` and also make the thrusts the real life values. Accelerations will be correctly scaled as well.*
- `use_vertical_speed_scale` | BOOLEAN | **false** | *If `true`, all vertical in game speeds will be 1/8th the speed parameters in this preset file.
  The 1/8th scale value will be configurable in the future. If you are making a custom fighter jet, set this to `true`, and make all speeds like `max_speed`
  and `cruise_speed` the real life speeds in `meters/tick` and also make the thrusts the real life values. Accelerations will be correctly scaled as well.*
- `break_deacc_ground` | NUMBER | **0.005** | *The de-acceleration applied to a vehicle while using breaks on the ground in `meters/tick^2`*
- `break_deacc_air` | NUMBER | **0.001** | *The de-acceleration applied to a vehicle while using breaks in the air in `meters/tick^2`*
- `min_drive_acc` | NUMBER | **0** | *The minimum ground acceleration applied to a vehicle when driving at full throttle. This can be used on any driving vehicle, but especially planes to help them reach take off speed faster.*
- `mass` | NUMBER | **1000** | *Determined the vehicle's weight. Must be positive!*
- `stealth` | NUMBER | **1** | *A stealth value of 0 means the vehicle is invisible to radars. 1 means no stealth. Values greater than 1 make it easier for radars to see this vehicle. Values less than 1 make it harder for radars to see. Must be positive!*
- `cross_sec_area` | NUMBER | **10** | *Larger values mean more air resistance and make it easier for a radar to detect. Must be positive!*
- `drag_area` | NUMBER | **cross_sec_area** | *The surface area of a vehicle used to calculate drag in `meters^2`.
  `cross_sec_area` is now separate and will be used for radar mechanics. Also note that drag for planes is more complex.*
- `idleheat` | NUMBER | **10** | *Heat determines how likely a heat seeking missile will target this vehicle. The larger the value, the more likely to be targeted. `idleheat` is the passive heat emission from the vehicle. Note the vehicle will get hotter when the engines are running. Must be positive!*
- `base_armor` | NUMBER | **0** | *The maximum armor this vehicle can have. Must be positive!*
- `armor_damage_threshold` | NUMBER | **0** | *The minimum damage that must be inflicted on a vehicle with armor, before it starts taking damage. Must be positive!*
- `armor_damage_absorbtion` | NUMBER | **0** | *Must be a value between 0 and 1! The percentage of damage reduced from attacks when the vehicle has armor.*
- `max_altitude` | NUMBER | **330** | *The maximum altitude a vehicle is allowed to reach. Note: altitude is distance from sea level. Sea level in the overworld is y = 70. So the default max y coordinate is 400.*
- `throttleup` | NUMBER | **0.01** | *Throttle is a number that can be between 0 and 1. This is the amount throttle increases per tick when the player inputs Throttle Increase. Must be positive!*
- `throttledown` | NUMBER | **0.01** | *Throttle is a number that can be between 0 and 1. This is the amount throttle decreases per tick when the player inputs Throttle Decrease. Must be positive!*
- `negativeThrottle` | BOOLEAN | **false** | *If true, throttle can be between -1 and 1 allowing for backwards thrust. Used in cars, boats, and submarines. Should be kept as false in planes and helicopters.*
- `turn_radius` | NUMBER | **100** | *The minimum turn radius when the vehicle drives on the ground. Must be positive!*
- `maxroll` | NUMBER | **0** | *The maximum change in roll that the vehicle can perform when the player inputs roll left or right in degrees per tick. Must be positive!*
- `maxpitch` | NUMBER | **0** | *The maximum change in pitch that the vehicle can perform when the player inputs pitch up or down in degrees per tick. Must be positive!*
- `maxyaw` | NUMBER | **0** | *The maximum change in yaw that the vehicle can perform when the player inputs yaw left or right in degrees per tick. Must be positive!*
- `torqueroll` | NUMBER | **0** | *Controls how quickly the vehicle can accelerate into its maximum roll rate. Used by aircraft and watercraft. Must be positive!*
- `torquepitch` | NUMBER | **0** | *Controls how quickly the vehicle can accelerate into its maximum pitch rate. Used by aircraft and watercraft. Must be positive!*
- `torqueyaw` | NUMBER | **0** | *Controls how quickly the vehicle can accelerate into its maximum yaw rate. Used by aircraft and watercraft. Must be positive!*
- `inertiaroll` | NUMBER | **4** | *Controls how much "resistance" there is to turning on the roll axis. Must be greater than zero!*
- `inertiapitch` | NUMBER | **4** | *Controls how much "resistance" there is to turning on the pitch axis. Must be greater than zero!*
- `inertiayaw` | NUMBER | **4** | *Controls how much "resistance" there is to turning on the yaw axis. Must be greater than zero!*
- `has_turn_assist` | BOOLEAN | **false** | *Set to `true` if the pane should have a turn assist. Normally used for modern fighter jets.
  Also known as a 'Rate Limiter'.*
- `hard_coded_rot_acc` | VEC3 | **OPTIONAL** | *A rotational acceleration (`degrees/tick^2`) override for each
  rotational axis (X->pitch,Y->yaw,Z->roll) based on player inputs. Use in combination with `maxroll`, `maxpitch`, `maxyaw`, and `hard_coded_rot_decel`.
  This parameter is meant for all vehicles other than planes and cars (helis, boats, submarines, tanks).
  Note that cars should still used `turn_radius`.
  Finding the right `inertia` and `torque` values can be annoying for vehicles like boats that you just want to have simple turn mechanics.*
- `hard_coded_rot_decel` | NUMBER | **OPTIONAL** | *The rate in `degrees/tick^2` a vehicle will rotationally de-accelerate back to 0.
  See `hard_coded_rot_acc` for use cases.*
- `crashExplosionRadius` | NUMBER | **0** | *The radius of an explosion if the vehicle crashes into an obstacle. If no explosion is wanted then keep it at zero.*
- `cameraDistance` | NUMBER | **4** | *The distance the pilot's third person camera is from the player head. The vanilla/default distance is 4. Must be greater than zero!*
- `mastType` | ENUM | **NONE** | *The kind of mast that external radars will sit on. Mostly used for boats. Options: `NONE`, `THIN`, `NORMAL`, `LARGE`*
- `rootHitboxNoCollide` | BOOLEAN | **false** | *If true, entities no longer collide with the main hitbox of the vehicle and players cant damage or interact with it either. Entities will only collide with the rotable hitboxes within `hitboxes` instead. Players can interact/right click the first hitbox in the `hitboxes` list. The root hitbox will still handle block collisions regardless. When you enable hitboxes (F3+B) the root hitbox is the white box, and the rotable `hitboxes` are the orange boxes.*
- `entity_size_xz` | NUMBER | **4** | *The horizontal size of the root hitbox. Must be greater than zero!*
- `entity_size_y` | NUMBER | **4** | *The vertical size of the root hitbox. Must be greater than zero!*
- `groundXTilt` | NUMBER | **0** | *The angle in degrees that the vehicle should tilt up when on the ground. Used in WW2 style planes where the nose points up while on the ground.*
- `hitboxes_control_pitch` | STRING_ARRAY | **OPTIONAL** | *A list of names from `hitboxes`. If all these boxes are destroyed, the player can no longer control the vehicle's pitch.*
- `hitboxes_control_yaw` | STRING_ARRAY | **OPTIONAL** | *A list of names from `hitboxes`. If all these boxes are destroyed, the player can no longer control the vehicle's yaw.*
- `hitboxes_control_roll` | STRING_ARRAY | **OPTIONAL** | *A list of names from `hitboxes`. If all these boxes are destroyed, the player can no longer control the vehicle's roll.*
- `max_push_thrust_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific push thrust per engine. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `max_afterburner_push_thrust_per_engine` | NUMBER | **max_push_thrust_per_engine** | *Afterburner thrust per engine in Newtons.
  If this is set to a value higher than `max_push_thrust_per_engine`, then the vehicle will be able to turn on an Afterburner.*
- `max_spin_thrust_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific spin thrust per engine. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `heat_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific engine heat. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `fuel_consume_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific fuel consumed per tick per engine. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `physics_components` | JSON_OBJECT_ARRAY | **OPTIONAL** | *An array of simulated physics instances that contribute to the vehicle's net forces and moments.
  One type of physics component is a lift surface. Scroll down for more information.
  Additionally, please look at [some examples](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185) to see how they are defined.*
- `plane` | JSON_OBJECT | **REQUIRED FOR PLANES** | *Stats that only planes used are stored here.*
    - `flapsAOABias` | NUMBER | **8** | *If the plane's flaps are down, the plane's AOA is increased by this value in degrees. Some planes need an AOA greater than zero to take off, so they need their flaps to be down.*
    - `canAimDown` | BOOLEAN | **false** | *If true, a plane can press the Special 2 key to point the nose gun down about 25 degrees.*
    - `wing_area` | NUMBER | **10** | *Surface area of the plane's wings. A higher value means more lift generated from the wings. Must be positive!*
    - `fuselage_lift_area` | NUMBER | **0** | *Surface area of the plane's fuselage. A higher value means more lift generated from the fuselage. Must be positive!*
    - `wing_lift_k_graph` | STRING | **fuselage** | *A stat graph id. Must be a stat graph with a `presetType` of `aoaliftk`. [Stat Graphs that come with the mod.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/data/dscombat/stat_graph) You can always add your own! Determines the lift coefficient for the wings based on AOA.*
    - `fuselage_lift_k_graph` | STRING | **fuselage** | *A stat graph id. Must be a stat graph with a `presetType` of `aoaliftk`. [Stat Graphs that come with the mod.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/data/dscombat/stat_graph) You can always add your own! Determines the lift coefficient for the fuselage based on AOA.*
    - `turn_rates_graph` | STRING | **wooden_plane_turn_rates** | *A stat graph id. Must be a stat graph with a `presetType` of `turn_rates_speed`. [Stat Graphs that come with the mod.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/data/dscombat/stat_graph) You can always add your own! Determines the max turn rate of the plane vs speed.*
    - `wing_lift_hitbox_names` | STRING_ARRAY | **OPTIONAL** | *A list of names from `hitboxes`. The percentage of hitboxes in this list that are still alive, is the percentage of `wing_area` used to generate lift.*
    - `aoa_drag_factor` | NUMBER | **1** | *Scale how much drag high AOA causes.*
    - `centripetal_scale` | NUMBER | **0.4** | *Scale the horizontal force wings generate.*
    - `drag_aoa_graph_key` | STRING | **default_drag_aoa** | *A stat graph id. The drag vs aoa graph for the main plane body. Not as important as how the lift surfaces in `physics_components` are set up.*
    - `centripetal_scale` | NUMBER | **1** | *Use if you want to increase or decrease the centripetal forces cause by the plane's lift surfaces.*
- `heli` | JSON_OBJECT | **REQUIRED FOR HELICOPTERS** | *Stats that only helicopters use are stored here.*
    - `heliLiftFactor` | NUMBER | **1** | *Make this value bigger if you want the helicopter to support more weight. Must be positive!*
    - `alwaysLandingGear` | BOOLEAN | **false** | *If true, the helicopter's landing gear is always active.*
- `car` | JSON_OBJECT | **REQUIRED FOR CARS** | *Stats that only cars use are stored here.*
    - `isTank` | BOOLEAN | **false** | *If true, the vehicle will use tank drive physics instead of car drive physics.*

`slots` | JSON_OBJECT_ARRAY | **REQUIRED** | *Set all the slots and their part items in this list. YOU MUST ADD ONE, AND ONLY ONE, PILOT SEAT! Please look at [some examples](https://github.com/1whohears/DiamondStarCombat/blob/c8af802ba55b345cda7953846bca5734c18822a7/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L187) to understand how slots work. See **Dev Commands** to test slot positions in game.*
- `name` | STRING | **REQUIRED** | *ALL SLOTS MUST HAVE UNIQUE NAMES! Add `"slotname.dscombat.[name]": "Name"` to a lang file if the name doesn't have a translation already. There are a few names with unique behavior...`pilot_seat`: Riders of this seat control everything in the vehicle. There can only be ONE pilot seat! `copilot_seat`: Riders of this seat can ONLY shoot weapons in the vehicle.*
- `slot_type` | STRING | **REQUIRED** | *Determines what kind of parts can be put in this slot. If you hover the mouse cursor above the part item, you can see what slot types the part is compatible with. Options: `seat`, `external`, `external_tough`, `mount_light`, `mount_med`, `mount_heavy`, `mount_tech`, `pylon_light`, `pylon_med`, `pylon_heavy`, `internal`, `tech_internal`, `high_tech_internal`, `internal_gun`, `spin_engine`, `push_engine`, `radial_engine`*
- `slot_posx` | NUMBER | **0** | *Where on the vehicle, this slot is positioned. Used for external parts like seats, turrets, and weapons.*
- `slot_posy` | NUMBER | **0** | *Where on the vehicle, this slot is positioned. Used for external parts like seats, turrets, and weapons.*
- `slot_posz` | NUMBER | **0** | *Where on the vehicle, this slot is positioned. Used for external parts like seats, turrets, and weapons.*
- `zRot` | NUMBER | **0** | *The rotation of the part in degrees based of what side of the vehicle the part is connected too. If `0`, the part is connected to the top of the vehicle. If `180`, the part is connected to the bottom of the vehicle. If `90`, connected to the right side. If `-90`, connected to the left side. For example, pylons often have `zRot` set to `180` because they are positioned under the wing.*
- `locked` | BOOLEAN | **false** | *If true, the player will not be allowed to modify the part in this slot.*
- `onlyCompatPart` | STRING | **OPTIONAL** | *If set, only part items with this `presetId` will be allowed to be put in this slot.*
- `linkedHitbox` | STRING | **OPTIONAL** | *If set, if a hitbox from `hitboxes` with this `name` is destroyed, the part in this slot will get damaged.*
- `data` | JSON_OBJECT | **OPTIONAL** | *Part data goes in here. Don't include if you want the slot to be empty.*
    - `part` | STRING | **REQUIRED?** | *Either `part` or `itemid` is required, not both. A Part `presetId`. See the [Part Presets in the mod](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/parts) for a list of options. Example `seat`.*
    - `itemid` | RESOURCE_LOCATION | **REQUIRED?** | *Either `part` or `itemid` is required, not both. A part item resource location. Example: `dscombat:seat`.*
    - `filled` | BOOLEAN | **false** | *If true, the part will be filled in brand new instances of this vehicle.*
    - `param` | STRING | **OPTIONAL** | *If `filled` is true, some parts need an additional parameter to know what to fill the part with. For example, weapons and turrets need a Weapon's `presetId`.*

`hitboxes` | JSON_OBJECT_ARRAY | **OPTIONAL** | *The list of custom hitboxes. Each hitbox must have a unique name! If `rootHitboxNoCollide` is true, the first hitbox in this list should be a central or fuselage hitbox. The first hitbox in the list would be what the player interacts with. See **Dev Commands** to test hitbox positions and sizes in game.*
- `name` | STRING | **REQUIRED** | *Each hitbox must have a unique name! Used as an id by other parts of the mod to check if certain hitboxes were destroyed.*
- `size` | VEC3 | **REQUIRED** | *The size of the hitbox. Assumes the vehicle is facing directly south. Thus, positive Z is forward, positive X is left, and positive Y is up.*
- `rel_pos` | VEC3 | **REQUIRED** | *The position of the hitbox relative to the vehicle's origin. Assumes the vehicle is facing directly south. Thus, positive Z is forward, positive X is left, and positive Y is up.*
- `max_health` | NUMBER | **0** | *If greater than zero, the hitbox will be destroyed if its health is reduced to zero.*
- `max_armor` | NUMBER | **0** | *The armor stat will take damage before the health.*
- `remove_on_destroy` | BOOLEAN | **false** | *If true, the hitbox will stop colliding with entities if its health is reduced to zero.*
- `damage_parts` | BOOLEAN | **false** | *If true, the hitbox will start randomly damaging parts with `linkedHitbox` equal to `name` at %50 health. All of those parts will be destroyed at zero health.*
- `damage_root` | BOOLEAN | **false** | *If true, the hitbox and the root vehicle health will eat the same damage.*

`ingredients` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of ingredients needed to craft the vehicle in the Vehicle Workbench. You need to add a recipe json file to the recipes folder in order for these vehicles to appear in the Vehicle Workbench. Scroll down to Vehicle Recipes.*
- `num` | INTEGER | **REQUIRED** | *The number of this item needed to craft.*
- `item` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. The item id of the ingredient. Example: `minecraft:iron_ingot`*
- `tag` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. If more than one item is compatible with this ingredient, use a tag id. Example: `minecraft:planks`*

`textures` | JSON_OBJECT | **OPTIONAL** | *If you right click a vehicle with a spray can, a custom texture menu pops up. Where the textures are stored will be explained in the assets section.*
- `baseTextureVariants` | INTEGER | **1** | *How many base textures/skins are available. One can cycle through these in the spray can menu. Must be 1 or more!*
- `textureLayers` | INTEGER | **0** | *How many layer textures are available. Must be positive!*

`paintjob_color` | INTEGER | **0** | *This parameter name is misleading and needs to be changed. It is the default base texture index. So if a vehicle has 3 base textures, and you want this preset to always start with the 2nd one, then `paintjob_color` = 1.*

`after_burner_smoke` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of positions relative to the vehicle's origin for where after burner particles should appear.*
- `pos` | VEC3 | **REQUIRED** | *Position relative to the vehicle's origin. Assumes the vehicle is facing directly south. Thus, positive Z is forward, positive X is left, and positive Y is up.*

`sounds` | JSON_OBJECT | **REQUIRED** | *Define what sounds this vehicle uses.*
- `passengerSoundPack` | STRING | **no_voice** | *Which bitchin betty do you want to listen to? Options: `no_voice`, `eng_non_binary_goober`, `eng_male_1`*
- `loopSoundType` | STRING | **basic** | *The type of "sound engine" this vehicle uses. Depending on this param, you will need to set additional sounds. Options: `basic`, `fighter_jet`. [Basic Sounds Example.](https://github.com/1whohears/DiamondStarCombat/blob/c8af802ba55b345cda7953846bca5734c18822a7/src/generated/resources/data/dscombat/vehicle/mrbudger_tank_empty.json#L118) [Fighter Jet Sounds Example.](https://github.com/1whohears/DiamondStarCombat/blob/c8af802ba55b345cda7953846bca5734c18822a7/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L332)*

#### `physics_components`

The stat property `physics_components` is an array of simulated physics instances that contribute to the vehicle's net forces and moments.
Please look at [some examples](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185) to see how they are defined.
See below for specifics on each Physics Component type.

The following are parameters that every Physics Component Type has:

`hitbox` | String | **NONE** | *If set to NONE, then this physics instance is active as long as the vehicle is operational.
One can set this to a hitbox name from `hitboxes` so that this physics instance is active as long as that hitbox is still alive/exists.*

`pos` | VEC3 | **OPTIONAL** | *The position in `meters` relative to the vehicle's origin that this physics instance operates. Assumes zeros if left empty.*

##### Lift Surface

Lift Surfaces will generate lift and induced drag for their parent aircraft based on Angle of Attack and velocity.

Planes now only rotate if there is a difference in forces among the lift surfaces. When all lift surfaces are stable, there is zero net torque.
When a lift surface rotates, the angle of attack for that surface becomes different than the rest, leading to a non zero net torque, causing rotation.
So the `input_type` parameter allows the lift surface to become a control surface by rotating it based on player inputs.

If you look at the [Alexis Plane's set of Lift Surfaces](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185),
you will notice that where ever there is an `ELEVATOR` or `STABILIZER` on the tail side of the aircraft, there is an equally sized lift surfce on the
opposite side of the vehicle. This is to keep the aircraft stable if the pilot is not inputting anything.

`id` = `lift_surface`

`area` | NUMBER | **10** | *The surface area in `meter^2` of this lift surface.*

`rotation` | VEC3 | **OPTIONAL** | *The default rotation of the lift surface relative to the vehicle in degrees.
Leaving all as zero leaves the lift surface parallel with the ground.
If the lift surface is meant to be a tail stabilizer, then one should set the Z component to 90.*

`input_type` | ENUM | **NONE** | *Options: `NONE`, `LEFT_FLAP`, `RIGHT_FLAP`, `ELEVATOR`, `STABILIZER`.
The type of control surface. For example, `ELEVATOR` rotates up and down based on pitch inputs.
`STABILIZER` rotates based on yaw inputs, and the flaps rotate based on roll inputs.*

`input_rotation_max` | NUMBER | **4** | *How much the lift surface rotates in degrees when the maximum angular input is used.*

`ignore_roll` | BOOLEAN | **false** | *If `true`, the lift surface will not rotate on the roll axis when the plane rolls.
Use this to make a 'fuselage' lift surface so that a fighter jet can stay in the air even rolled 90 degrees.*

`lift_k_graph` | STRING | **fuselage** | *Lift vs AOA Stat graph id. Determines what Lift coefficient is used at the current AOA.
See the Stat Graph section for more information on these types of graphs.
See this [example Lift vs AOA graph for the Eden Plane.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_lift_aoa.json)*

`zero_lift_drag` | NUMBER | **0.5** | *Can be kinda thought of as the drag coefficient at 0 degrees AOA.*

`drag_graph` | STRING | **default_drag_aoa** | *Float vs Float Stat graph id. Determines what Drag Coefficient is used at the current AOA.
See the Stat Graph section for more information on these types of graphs.
See this [example Drag vs AOA graph for the Eden Plane.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_drag_aoa.json)*

### Dev Commands

These commands allow you to check sizes and positions in game. You have to sit in the pilot seat of a vehicle for them to work. The changes these commands make are not permanent! You must guess and check, once you find values you like you update the json file. The slot and hitbox need to exist already in both commands.

Also, for some reason the position coordinates are not accurate sometimes. Adding a small number like 0.00001 fixes it. Don't ask why because I have no idea.

#### Test Part Slot Positions

`/debugslotpos <slot_id> <rel_pos>`

#### Test Hitbox Sizes and Positions

`/debughitboxpos <hitbox_name> <size> <rel_pos>`

### Vehicle Recipes

In order for your custom vehicle to appear in the Vehicle Workbench you need to add a recipe json file to `//data/[namespace]/recipes/workbench_vehicle_[presetId].json`

An example of the Unarmed Alexis Plane recipe can be seen below. Replace `alexis_plane_unarmed` with your plane's `presetId`. The Vehicle Workbench will use the items defined in `ingredients` in the vehicle preset file.

```
{
  "type": "dscombat:aircraft_workbench",
  "presetId": "alexis_plane_unarmed"
}
```

## Assets

It is important that your vehicle has a unique `assetId` set in the Data Preset File explained above. `assetId` will be used everywhere to determine where assets are stored. All of the following files are required to successfully make a custom vehicle, unless stated otherwise.

### Vehicle Client Preset

**`vehicle_client_preset_file` = //assets/[`namespace`]/vehicle_client/[`assetId`].json**

Vehicle Client Presets have the same format as Json Presets, but they are an asset. Thus they can be modified with resource packs and the server doesn't force synch this data. [Here are some examples.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/assets/dscombat/vehicle_client) Again, note that the file name has to be the same as `assetId`. The following are all the preset parameters for Vehicle Client Presets. The only `presetType` for Vehicle Client Presets is `standard`.

`inventory_background` | RESOURCE_LOCATION | **OPTIONAL** | *The background texture for the vehicle inventory.*

`inventory_slots_pos` | JSON_OBJECT_ARRAY | **OPTIONAL** | *Set the positions of the vehicle inventory slots in the UI. Useful if you want the slot positions to line up with the texture. Otherwise the slots are positioned in a grid.*
- `slot_name` | STRING | **REQUIRED** | *Must equal the `name` of a slot from `slots`.*
- `slot_ui_x` | INTEGER | **REQUIRED** | *Horizontal UI coordinate.*
- `slot_ui_y` | INTEGER | **REQUIRED** | *Vertical UI coordinate.*

`model_data` | JSON_OBJECT | **OPTIONAL** | *If not included, `model_id` will be assumed to be `assetId`, and there won't be any animations.*
- `model_id` | STRING | **`assetId`** | *The file name of the model used for this vehicle. Scroll down to the Models section for more information.*
- `custom_anims` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of custom animations. These animations manipulate individual groups/bones/model parts. multiple animations can be stacked on the same `model_part_key`! There are different animation types, each requiring different parameters. All the parameters will be listed below, but a bit further down an explanation of each animation type will be documented.*
    - `anim_id` | STRING | **REQUIRED** | *Each animation type will be explained below. Options: `continuous_rotation`, `motor_rotation`, `wheel_rotation`, `input_bound_rotation`, `spinning_radar`, `landing_gear`, `input_bound_translation`, `plane_flap_rotation`, `hitbox_destroy_part`.*
    - `model_part_key` | STRING | **REQUIRED** | *The name of the bone/group/object within the model that is being animated.*
    - `pivot` | VEC3 | **ZEROS** | *The pivot point a model part will rotate around. The units are Minecraft pixels or 1/16th of a block.*
    - `rot_axis` | ENUM | **X** | *The axis the model part rotates around. Options: `X`, `Y`, `Z`.*
    - `rot_rate` | NUMBER | **0** | *Maximum rotation rate in degrees per tick.*
    - `input_axis` | ENUM | **PITCH** | *The input axis that controls how much the part rotates. Options: `PITCH`, `YAW`, `ROLL`, `THROTTLE`*
    - `bound` | NUMBER | **0** | *How for the part rotates in degrees.*
    - `radar_id` | STRING | **OPTIONAL** | *The `presetId` of the radar that should spin.*
    - `fold_angle` | NUMBER | **0** | *The angle in degrees the landing gear part rotates while folding.*
    - `bounds` | VEC3 | **ZEROS** | *The max distance the model part will be translated.*
    - `hitbox_name` | STRING | **OPTIONAL** | *If a hitbox from `hitboxes` with this `name` gets destroyed, this model pat will disappear.*

#### Custom Animation Types

The following is a description of each Custom Animation Type, and what parameters they need. See `custom_anims` above for documentation on all the parameters.

`continuous_rotation` | `pivot`, `rot_axis`, `rot_rate` | *Continuously rotates the model part at `rot_rate` degrees per tick.*

`motor_rotation` | `pivot`, `rot_axis`, `rot_rate` | *Rotates the model part based on vehicle engine usage. `rot_rate` is the rotation speed in degrees per tick at max throttle. Used in propellers for helicopters, biplanes, submarines, ext...*

`wheel_rotation` | `pivot`, `rot_axis`, `rot_rate` | *Rotates the model part based on ground speed. `rot_rate` is the rotation speed in degrees per second when the vehicle is traveling at 1 block per tick. Used for wheels in cars, and tanks.*

`input_bound_rotation` | `pivot`, `rot_axis`, `input_axis`, `bound` | *Rotates the model part between -`bound` degrees and +`bound` degrees, based on the magnitude of the `input_axis`. Used in flaps for planes, steering wheels for cars, and joysticks.*

`plane_flap_rotation` | `pivot`, `rot_axis`, `input_axis`, `bound` | *Same as `input_bound_rotation`, but if a plane inputs flaps down the flaps point down and ignore the input.*

`spinning_radar` | `pivot`, `rot_axis`, `rot_rate`, `radar_id` | *Continuously spins the radar at `rot_rate` degrees per tick. Used on Axcel Truck and E3-Sentry.*

`landing_gear` | `pivot`, `rot_axis`, `fold_angle` | *Do nothing if landing gear are out. If the landing gear are folding in, rotate the landing gear from 0 to `fold_angle` degrees for a couple seconds then disappear.*

`input_bound_translation` | `bounds`, `input_axis` | *Translate the model part from its origin to `bounds` based on the `input_axis`. Used for pedals in planes, and cars.*

`hitbox_destroy_part` | `hitbox_name` | *If the hitbox in `hitboxes` with `name` = `hitbox_name` is destroyed, this model part will disappear. Used in wings on planes.*

### Textures

[Example textures from the Alexis Plane.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/assets/dscombat/textures/entity/vehicle/alexis_plane)

**`base_texture_file` = //assets/[`namespace`]/textures/entity/vehicle/[`assetId`]/base[`index`].png**

- There should be `baseTextureVariants` many base texture files.
- There must be at least 1!
- `index` counts from 0 to `baseTextureVariants`-1

**`layer_texture_file` = //assets/[`namespace`]/textures/entity/vehicle/[`assetId`]/layer[`index`].png**

- There should be `textureLayers` many layer texture files.
- `index` counts from 0 to `textureLayers`-1
- Layers function like decals. They shouldn't cover the entire model.
- If you want the player to have full control of a color, make those pixels `#FFFFFF` white.
- Must have the same resolution of `base_texture_file`.

### Screen Texture Map

[Observe this example Screen Map.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/assets/dscombat/textures/entity/vehicle/alexis_plane/screens.png)

**`screen_texture_map_file` = //assets/[`namespace`]/textures/entity/vehicle/[`assetId`]/screens.png**

- Must have the same resolution of `base_texture_file`.
- Maps UV coordinates on the texture to 3d model coordinates. Thus the screens can fit perfectly onto a vehicle's dash.
- The screen map reader looks for specific colors to register certain screens. No other colors mater.
    - `#008282`: Big Radar Screen
    - `#00FFFF`: Air Radar Screen
    - `#4CFF00`: Ground Radar Screen
    - `#7F0000`: Fuel Screen
    - `#FF00DC`: RWR Screen
    - `#0026FF`: Heading Screen
    - `#008718`: Turn Coordinator Screen
    - `#7F3300`: Attitude Indicator Screen
    - `#840084`: Altimeter Screen
    - `#FFD800`: Air Speed Screen

# Weapons

## Data

[Here are some example presets to get started with!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/weapons)

### Available Preset Types (`presetType`)

- `none`: Used for the "Safety" weapon option.
- `bullet`: Bullets are bullets.
- `bomb`: Drops a Christmas present that has no internal propulsion.
- `bunker_buster`: A bomb that breaks a bunch of blocks before exploding.
- `pos_missile`: A missile that goes to the position the pilot picks.
- `ir_missile`: A missile that tracks heat sources.
- `track_missile`: A missile that tracks the entity that the player selects with their radar.
- `torpedo`: Same as track missile, but it doesn't explode in water.
- `dumb_torpedo`: Does nothing in the air, goes straight when it touches water.
- `anti_radar_missile`: These missiles target radar sources.

### All Weapons Parameter List

`maxAge` | NUMBER | **0** | *The max age in ticks before the weapon entity despawns.*

`fireRate` | NUMBER | **0** | *The length of cooldown in ticks before another weapon can be fired.*

`canShootOnGround` | BOOLEAN | **false** | *If true, this weapon can be shot while the owner vehicle is on the ground.*

`entityTypeKey` | RESOURCE_LOCATION | **OPTIONAL** | *The entity id that this wepaon uses. DO NOT OVERRIDE unless you coded a custom entity type!*

`shootSoundKey` | RESOURCE_LOCATION | **dscombat:bullet_shoot_1** | *The sound that plays when this weapon is fired.*

`compatibleWeaponPart` | STRING_ARRAY | **OPTIONAL** | *A list of Weapon Parts Ids and/or Turret Ids. These are the "guns" that this weapon is compatible with. [See here for a list of all parts!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/parts)*

`itemKey` | RESOURCE_LOCATION | **dscombat:ammo** | *Can also be set to `dscombat:bullet`, `dscombat:bomb`, or `dscombat:missile`.*

`assetId` | STRING | **`presetId`** | *Either the file name of the model that this weapon uses
(Scroll down to the Models section for more info), or the name of the Weapon Client Preset file used to
define custom weapon animations.*

`icon` | FULL_RESOURCE_LOCATION | **dscombat:textures/ui/weapon_icons/default.png** | *The texture that the weapon select UI uses. All weapon preset types already have their own default texture. But you can override it.*

`craftNum` | NUMBER | **0** | *How many ammo items are crafted from one set of `ingredients`.*

`ingredients` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of ingredients needed to craft the weapon in the Weapons Workbench. You need to add a recipe json file to the recipes folder. Scroll down to Weapon Recipes.*
- `num` | INTEGER | **REQUIRED** | *The number of this item needed to craft.*
- `item` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. The item id of the ingredient. Example: `minecraft:iron_ingot`*
- `tag` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. If more than one item is compatible with this ingredient, use a tag id. Example: `minecraft:planks`*

### Bullet Parameter List

`presetType` = `bullet`

Includes all stats listed in **All Weapons Parameter List**.

`damage` | NUMBER | **0** | *How much damage the bullet does.*

`speed` | NUMBER | **0** | *The speed in blocks per tick the bullet moves.*

`inaccuracy` | NUMBER | **0** | *When the bullet is fired a random degree angle between 0 and this value is selected which is added to the aiming angle.*

`explosive` | BOOLEAN | **false** | *If this bullet explodes when it hits the ground or entity.*

`destroyTerrain` | BOOLEAN | **false** | *If the explosion breaks blocks.*

`causesFire` | BOOLEAN | **false** | *If the explosion causes fire.*

`explosionRadius` | NUMBER | **0** | *The size in blocks of the explosion.*

`explodeNum` | NUMBER | **1** | *How many times the bullet explodes when it hits the ground or an entity.*

### Bomb Parameter List

`presetType` = `bomb`

Includes all stats listed in **All Weapons Parameter List**, and **Bullet Parameter List**.

### Bunker Buster Parameter List

`presetType` = `bunker_buster`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Bomb Parameter List**.

`blockStrength` | NUMBER | **0** | *A block's mining strength subtracts from this value. Once it reaches zero it explodes.*

### Missile Parameter List

Includes all stats listed in **All Weapons Parameter List**, and **Bullet Parameter List**

`turnRadius` | NUMBER | **500** | *The turn radius of missiles in blocks.*

`acceleration` | NUMBER | **0** | *The acceleration of missiles in blocks/tick/tick.*

`fuseDist` | NUMBER | **1** | *If the missile is within this distance of its target, it will detonate.*

`fov` | NUMBER | **-1** | *Set to -1 to allow the missile to see targets all around it. Any other values lets it see in that degree field of view.*

`bleed` | NUMBER | **0** | *A constant where the higher the value, the faster the missile looses its speed when turning.*

`fuelTicks` | NUMBER | **`maxAge`** | *If a missile has fuel, it will continue to accelerate. When it runs out of fuel after this many ticks it will start loosing speed based on `bleed`.*

`seeThroWater` | NUMBER | **0** | *How many blocks of water a missile can see its target through.*

`seeThroBlock` | NUMBER | **0** | *How many solid blocks a missile can see its target through.*

### Position Missile Parameter List

`presetType` = `pos_missile`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Missile Parameter List**.

### IR Missile Parameter List

`presetType` = `ir_missile`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Missile Parameter List**.

`flareResistance` | NUMBER | **1** | *Must be 0 or greater. 0 makes it ignore flares. Less than 1 makes it less likely to target a flare. 1 makes it treat flares and heat from vehicles equally. Greater than 1 makes it more likely to target a flare.*

### Track Missile Parameter List

`presetType` = `track_missile`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Missile Parameter List**.

`targetType` | ENUM | **AIR** | *If the missile can target AIR, GROUND, or WATER targets.*

`activeTrack` | BOOLEAN | **true** | *If `true`, the missile can track targets with its own radar. If `false`, the parent vehicle's radar has to guide the missile to the target.*

### Torpedo Missile Parameter List

`presetType` = `torpedo`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, **Missile Parameter List**, and **Track Missile Parameter List**.

### Dumb Torpedo Missile Parameter List

`presetType` = `dumb_torpedo`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Missile Parameter List**.


### Anti Radar Missile Parameter List

`presetType` = `anti_radar_missile`

Includes all stats listed in **All Weapons Parameter List**, **Bullet Parameter List**, and **Missile Parameter List**.

`scan_range` | NUMBER | **0** | *How far in blocks can this missile see radar sources.*

### Weapon Recipes

In order for your custom weapon to appear in the Weapons Workbench you need to add a recipe json file to `//data/[namespace]/recipes/workbench_weapon_[presetId].json`

An example of the 10mm Bullet recipe can be seen below. Replace `10mm` with your weapon's `presetId`. The Weapons Workbench will use the items defined in `ingredients` in the weapon preset file.

```
{
  "type": "dscombat:weapon_workbench",
  "presetId": "10mm"
}
```

## Assets

All Weapons support the Obj Model custom animation system. It functions nearly identically to the vehicle
client preset system. Weapon Client preset files are only needed if you want to add custom animations.
If a Weapon Client Preset file is not created, the `model_id` will be assumed to be the `assetId` defined
in the Datapack json file.

### Weapon Client Preset

Weapon Client Presets must have a `presetId` equal to `assetId` as defined in the Datapack portion above.
The file is located here:

**`weapon_client_preset_file` = //assets/[`namespace`]/part_client/[`assetId`].json**

Part Client Presets have the same format as Json Presets, but they are an asset.
Thus, they can be modified with resource packs and the server doesn't force syncing this data.
[Here are some examples.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/assets/dscombat/weapon_client)
Again, note that the file name has to be the same as `presetId`.

#### Available Preset Types

Weapons Client Presets currently only use one `presetType`:

- `standard`

#### All Weapon Client Preset Parameters

`model_data` | JSON_OBJECT | **OPTIONAL** | *If not included, `model_id` will be assumed to be `assetId`, and there won't be any animations.*
- `model_id` | STRING | **`assetId`** | *The file name of the model used for this vehicle. Scroll down to the Models section for more information.*
- `custom_anims` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of custom animations. These animations manipulate individual groups/bones/model parts. multiple animations can be stacked on the same `model_part_key`! There are different animation types, each requiring different parameters. All the parameters will be listed below, but a bit further down an explanation of each animation type will be documented.*
    - `anim_id` | STRING | **REQUIRED** | *Each animation type will be explained below. Options: `continuous_rotation`, `motor_rotation`, `wheel_rotation`, `input_bound_rotation`, `spinning_radar`, `landing_gear`, `input_bound_translation`, `plane_flap_rotation`, `hitbox_destroy_part`.*
    - `model_part_key` | STRING | **REQUIRED** | *The name of the bone/group/object within the model that is being animated.*
    - `pivot` | VEC3 | **ZEROS** | *The pivot point a model part will rotate around. The units are Minecraft pixels or 1/16th of a block.*
    - `rot_axis` | ENUM | **X** | *The axis the model part rotates around. Options: `X`, `Y`, `Z`.*
    - `rot_rate` | NUMBER | **0** | *Maximum rotation rate in degrees per tick.*
    - `input_axis` | ENUM | **PITCH** | *The input axis that controls how much the part rotates. Options: `PITCH`, `YAW`, `ROLL`, `THROTTLE`*
    - `bound` | NUMBER | **0** | *How for the part rotates in degrees.*
    - `radar_id` | STRING | **OPTIONAL** | *The `presetId` of the radar that should spin.*
    - `fold_angle` | NUMBER | **0** | *The angle in degrees the landing gear part rotates while folding.*
    - `bounds` | VEC3 | **ZEROS** | *The max distance the model part will be translated.*
    - `hitbox_name` | STRING | **OPTIONAL** | *If a hitbox from `hitboxes` with this `name` gets destroyed, this model pat will disappear.*
    - `rotPitch` | BOOLEAN | **true** | *If `rotPitch` = `true` then the animation will follow the up and down rotation of the model. If `rotPitch` = `false` then the animation will follow the left and right rotation of the model.*


# Radars

WIP. A lot of the current radar data gen will be outdated once radar mechanics are revamped. [Please see these examples for now!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/radars)

# Parts

## Data

[Here are some example presets to get started with!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/parts)

### Available Preset Types (`presetType`)

- `internal_weapon`: A weapon that will appear in the pilot weapon system. Does NOT have an external entity weapon rack.
- `external_weapon`: A **Weapon Part** that will appear in the pilot weapon system. Has an external entity weapon rack. See Assets for making custom weapon rack models.
- `seat`: Invisible seat entity that players/mobs can sit in.
- `turret`: A **Weapon Part** that the player can sit in. See Assets for making custom turret models.
- `internal_engine`: An engine that doesn't have an external entity.
- `external_engine`: An engine that has an external entity. (not implemented yet)
- `fuel_tank`: Internal fuel tank.
- `external_fuel_tank`: External fuel tank with an external model.
- `internal_radar`: Internal radar.
- `external_radar`: External radar with an external radar model.
- `flare_dispenser`: Drops flares.
- `chaff_dispenser`: Drops chaff. (not implemented yet)
- `buff`: Simple improvement part that is used for data link, night vision, radio, or extra armor.
- `gimbal`: An external gimbal camera that the pilot can see through use.
- `chain_hook`: A hook that other vehicles can be chained to.
- `internal_storage`: Used for internal storage boxes.
- `external_storage`: Used for external storage boxes that have external models. (not implemented yet)

### All Parts Parameter List

`weight` | NUMBER | **0** | *The weight this part will add to the vehicle. See the examples to see what values are typically used.*

`item` | RESOURCE_LOCATION | **REQUIRED** | *The item that this part uses. Scroll down to more details on each `presetType` for which item should be used.*

`slotType` | STRING | **internal** | *The type of slot this part is compatible with. Options: `seat`, `external`, `external_tough`, `mount_light`, `mount_med`, `mount_heavy`, `mount_tech`, `pylon_light`, `pylon_med`, `pylon_heavy`, `internal`, `tech_internal`, `high_tech_internal`, `internal_gun`, `spin_engine`, `push_engine`, `radial_engine`.*

`externalEntity` | RESOURCE_LOCATION | **OPTIONAL?** | *If the `presetType` is external, then scroll down for more details on that `presetType` to see what kind of entity should be used.*

`hitbox_width` | NUMBER | **0.8** | *If the part has an external entity, this is the width of its hitbox.*

`hitbox_height` | NUMBER | **0.8** | *If the part has an external entity, this is the height of its hitbox.*

`ingredients` | JSON_OBJECT_ARRAY | **OPTIONAL?** | *Most parts are obtained via crafting table except for `external_weapon` and `turret`. A list of ingredients needed to craft the part in the Weapon Parts Workbench. You need to add a recipe json file to the recipes folder. Scroll down to Part Recipes.*
- `num` | INTEGER | **REQUIRED** | *The number of this item needed to craft.*
- `item` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. The item id of the ingredient. Example: `minecraft:iron_ingot`*
- `tag` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. If more than one item is compatible with this ingredient, use a tag id. Example: `minecraft:planks`*

`repair_cost` | JSON_OBJECT_ARRAY | **OPTIONAL** | *If a part is damaged, these are additional items that need to be present in the crafting table to repair the part.*
- `num` | INTEGER | **REQUIRED** | *The number of this item needed to craft.*
- `item` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. The item id of the ingredient. Example: `minecraft:iron_ingot`*
- `tag` | RESOURCE_LOCATION | **REQUIRED?** | *Either `item` or `tag` is required, NOT BOTH. If more than one item is compatible with this ingredient, use a tag id. Example: `minecraft:planks`*

### Internal Weapon Parameter List

`presetType` = `internal_weapon`

Includes all stats listed in **All Parts Parameter List**.

`max` | INTEGER | **0** | *The max number of weapons/ammo this weapon can hold.*

`item` Options: `dscombat:internal_gun`

### External Weapon Parameter List

`presetType` = `external_weapon`

Includes all stats listed in **All Parts Parameter List** and **Internal Weapon Parameter List**.

`changeLaunchPitch` | NUMBER | **0** | *Increase the pitch relative to the vehicle's pitch that this weapon shoots. Useful for vertically launched missiles.*

`item` Options: `dscombat:external_weapon_part`

`externalEntity` Default: `dscombat:external_weapon_part`

Scroll down to assets section for information of custom animations and missile positions on custom pylons.

### Seat Parameter List

`presetType` = `seat`

Includes all stats listed in **All Parts Parameter List**.

`passenger_offset` | VEC3 | **[0,0,0]** | *The passenger's offset from the seat relative to the vehicle's/turret's direction.*

`item` Options: `dscombat:seat`

`externalEntity` Default: `dscombat:seat`

### Turret Parameter List

`presetType` = `turret`

Includes all stats listed in **All Parts Parameter List** and **Seat Parameter List**.

`maxHealth` | NUMBER | **0** | *How much health this turret will start with.*

`maxAmmo` | NUMBER | **0** | *The max ammo this turret can hold.*

`weaponOffset` | NUMBER or OBJECT | **0** | *The offset relative to the turret entities base of the initial position of the shot weapon. Can be a single number for Y-only offset (backward compatibility), or an object with x, y, z coordinates for full 3D positioning. Examples: `"weaponOffset": 1.3` or `"weaponOffset": {"x": 0.0, "y": 1.3, "z": 2.0}`*

`shootType` | ENUM | **`NORMAL`** | *Special hard coded firing instructions. Options: `NORMAL`, `MARK7`. (custom shoot types currently require code)*

`rotBounds` | JSON_OBJECT | **REQUIRED** | *Controls the rotation rate, and rotation bounds of the turret.*
- `rotRate` | NUMBER | **0** | *The max degrees per tick the turret can rotate.*
- `minRotX` | NUMBER | **0** | *The most degrees the turret can look down.*
- `maxRotX` | NUMBER | **0** | *The most degrees the turret can look up.*

`item` Options: `dscombat:turret`

`externalEntity` Default: `dscombat:turret`

Scroll down to assets section for information of custom animations.

### Internal Engine Parameter List

`presetType` = `internal_engine`

Includes all stats listed in **All Parts Parameter List**.

`thrust` | NUMBER | **0** | *How much force this engine provides at max throttle.*

`heat` | NUMBER | **0** | *How much heat this engine creates at max throttle.*

`fuelRate` | NUMBER | **0** | *How much fuel this engine consumes per tick at max throttle.*

`engineType` | ENUM | **`SPIN`** | *Options: `SPIN`, `PUSH`. Spin engines generate force for wheels/ground vehicles and helicopter blades. Push engines generate force for planes, boats, and submarines.*

`item` Options: `dscombat:c6_engine`, `dscombat:c12_engine`, `dscombat:turbofan_f25`, `dscombat:turbofan_f145`, `dscombat:turbofan_f39`, `dscombat:klimov_rd33`, `dscombat:cm_manly_52`, `dscombat:allison_v_1710`, `dscombat:compound_turbine`

### External Engine Parameter List

`presetType` = `external_engine`

Includes all stats listed in **All Parts Parameter List** and **Internal Engine Parameter List**.

`item` Options: `dscombat:cfm56`

`externalEntity` Default: `dscombat:external_engine`

### Internal Fuel Tank Parameter List

`presetType` = `fuel_tank`

Includes all stats listed in **All Parts Parameter List**.

`max` | NUMBER | **0** | *The max amount of fuel this tank can hold.*

`item` Options: `dscombat:light_fuel_tank`, `dscombat:heavy_fuel_tank`

### External Fuel Tank Parameter List

`presetType` = `external_fuel_tank`

Includes all stats listed in **All Parts Parameter List** and **Internal Fuel Tank Parameter List**.

`item` Options: Not yet implemented

`externalEntity` Default: Not yet implemented

### Internal Radar Parameter List

`presetType` = `internal_radar`

Includes all stats listed in **All Parts Parameter List**.

`radar` | STRING | **REQUIRED** | *The radar `presetId` from the Radar Super Type.*

`item` Options: `dscombat:ar500`, `dscombat:ar1k`, `dscombat:ar2k`, `dscombat:gr200`, `dscombat:gr400`, `dscombat:wr400`, `dscombat:wr1k`, `dscombat:gpr20`, `dscombat:gpr100`

### External Radar Parameter List

`presetType` = `external_radar`

Includes all stats listed in **All Parts Parameter List** and **Internal Radar Parameter List**.

`item` Options: `dscombat:air_scan_a`, `dscombat:air_scan_b`, `dscombat:survey_all_a`, `dscombat:survey_all_b`

`externalEntity` Default: `dscombat:external_radar`

### Flare Dispenser Parameter List

`presetType` = `flare_dispenser`

Includes all stats listed in **All Parts Parameter List**.

`max` | NUMBER | **0** | *The max amount of flares this dispenser can hold.*

`age` | NUMBER | **0** | *The max age in ticks one of these flares can last.*

`heat` | NUMBER | **0** | *The amount of heat a flare creates. Note: the heat decays slowly as it gets older.*

`item` Options: `dscombat:basic_flare_dispenser`

### Chaff Dispenser Parameter List

`presetType` = `chaff_dispenser`

Includes all stats listed in **All Parts Parameter List**.

`item` Options: (not yet implemented)

### Buff Parameter List

`presetType` = `buff`

Includes all stats listed in **All Parts Parameter List**.

`buffType` | ENUM | **`DATA_LINK`** | *The type of buff this part is. Options:*
- `DATA_LINK`: Radar pings found by this vehicle will be shared with team members. Note: If the gamerule `dataLinkAlwaysOn` = `true`, then this buff is automatically given to every vehicle.
- `NIGHT_VISION_HUD`: Gives pilots night vision.
- `RADIO`: If you right-click a vehicle with a music disk, the vehicle will start playing the song.
- `ARMOR`: Adds a little extra armor to the vehicle.

`item` Options: `dscombat:data_link`, `dscombat:night_vision_hud`, `dscombat:radio`, `dscombat:armor_piece`

### Gimbal Parameter List

`presetType` = `gimbal`

Includes all stats listed in **All Parts Parameter List**.

`item` Options: `dscombat:gimbal_camera`

`externalEntity` Default: `dscombat:gimbal_camera`

### Chain Hook Parameter List

`presetType` = `chain_hook`

Includes all stats listed in **All Parts Parameter List**.

`item` Options: `dscombat:chain_hook`

`externalEntity` Default: `dscombat:chain_hook`

### Internal Storage Parameter List

`presetType` = `internal_storage`

Includes all stats listed in **All Parts Parameter List**.

`size` | INTEGER | **0** | *The number of storage slots in this container.*

`item` Options: `dscombat:small_storage_box`, `dscombat:medium_storage_box`, `dscombat:large_storage_box`

### External Storage Parameter List

`presetType` = `external_storage`

Includes all stats listed in **All Parts Parameter List** and **Internal Storage Parameter List**.

`item` Options: (not yet implemented)

`externalEntity` Default: (not yet implemented)

### Part Recipes

In order for your custom weapon rack/turret to appear in the Weapon Parts Workbench you need to add a recipe json file to `//data/[namespace]/recipes/workbench_weapon_part_[presetId].json`

An example of the CIWS recipe can be seen below. Replace `ciws` with your plane's `presetId`. The Weapon Parts Workbench will use the items defined in `ingredients` in the part preset file.

```
{
  "type": "dscombat:weapon_parts_workbench",
  "presetId": "ciws"
}
```

## Assets

All External Parts support the Obj Model custom animation system. It functions nearly identically to the vehicle client preset system. __**External Weapon Racks, and Turrets require a Part Client Preset file.**__ You can also use the custom anims system for turrets. If a Part Client Preset file is not created, the `modelId` will be assumed to be `presetId`.

### Part Client Preset

Part client presets use the same `presetId` as their respective Datapack `presetId`. The file is located here:

**`part_client_preset_file` = //assets/[`namespace`]/part_client/[`presetId`].json**

Part Client Presets have the same format as Json Presets, but they are an asset. Thus, they can be modified with resource packs and the server doesn't force syncing this data. [Here are some examples.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/assets/dscombat/part_client) Again, note that the file name has to be the same as `presetId`.

#### Available Preset Types

Unlike Vehicle Client Presets, Part Client Presets have more than one `presetType`:

- `standard`: External parts like engines, and chain hooks (they are visually just static models) use this type.
- `turret`: Used by turrets.
- `radar`: Used by external radars.
- `weapon_rack`: Used by external weapons/missile racks.

#### All Part Client Preset Parameters

`model_data` | JSON_OBJECT | **OPTIONAL** | *If not included, `model_id` will be assumed to be `assetId`, and there won't be any animations.*
- `model_id` | STRING | **`assetId`** | *The file name of the model used for this vehicle. Scroll down to the Models section for more information.*
- `custom_anims` | JSON_OBJECT_ARRAY | **OPTIONAL** | *A list of custom animations. These animations manipulate individual groups/bones/model parts. multiple animations can be stacked on the same `model_part_key`! There are different animation types, each requiring different parameters. All the parameters will be listed below, but a bit further down an explanation of each animation type will be documented.*
    - `anim_id` | STRING | **REQUIRED** | *Each animation type will be explained below. Options: `continuous_rotation`, `motor_rotation`, `wheel_rotation`, `input_bound_rotation`, `spinning_radar`, `landing_gear`, `input_bound_translation`, `plane_flap_rotation`, `hitbox_destroy_part`.*
    - `model_part_key` | STRING | **REQUIRED** | *The name of the bone/group/object within the model that is being animated.*
    - `pivot` | VEC3 | **ZEROS** | *The pivot point a model part will rotate around. The units are Minecraft pixels or 1/16th of a block.*
    - `rot_axis` | ENUM | **X** | *The axis the model part rotates around. Options: `X`, `Y`, `Z`.*
    - `rot_rate` | NUMBER | **0** | *Maximum rotation rate in degrees per tick.*
    - `input_axis` | ENUM | **PITCH** | *The input axis that controls how much the part rotates. Options: `PITCH`, `YAW`, `ROLL`, `THROTTLE`*
    - `bound` | NUMBER | **0** | *How for the part rotates in degrees.*
    - `radar_id` | STRING | **OPTIONAL** | *The `presetId` of the radar that should spin.*
    - `fold_angle` | NUMBER | **0** | *The angle in degrees the landing gear part rotates while folding.*
    - `bounds` | VEC3 | **ZEROS** | *The max distance the model part will be translated.*
    - `hitbox_name` | STRING | **OPTIONAL** | *If a hitbox from `hitboxes` with this `name` gets destroyed, this model pat will disappear.*
    - `rotPitch` | BOOLEAN | **true** | *If `rotPitch` = `true` then the animation will follow the up and down rotation of the model. If `rotPitch` = `false` then the animation will follow the left and right rotation of the model.*

#### Turret Client Preset Parameters

`rot_all_yaw` | BOOLEAN | **true** | *If `true`, the turret renderer will rotate every model component to the y angle. If `false`, custom animations will be needed to rotate the model.*

#### Radar Client Preset Parameters

`large_model_id` | STRING | **OPTIONAL** | *If the parent vehicle uses a large mast to support their external radars, use this alternative `modelId` to render the external radar.*

#### Weapon Rack Client Preset Parameters

`weapon_pos` | VEC3_ARRAY | **OPTIONAL** | *An array of positions relative to the model root that weapon models should be positioned.*

<details>

<summary>`weapon_pos` example: </summary>

```
"weapon_pos": [
  {
    "x":-0.5,
    "y":-0.2,
    "z":0,
  },
  {
    "x":0.5,
    "y":-0.2,
    "z":0,
  }
]
```

</details>

### Custom Animation Types

The following is a description of each Custom Animation Type compatible with parts, and what parameters they need. See `custom_anims` above for documentation on all the parameters.

`continuous_rotation` | `pivot`, `rot_axis`, `rot_rate` | *Continuously rotates the model part at `rot_rate` degrees per tick.*

`turret_rotation` | `pivot`, `rot_axis`, `rotPitch` | *Control which parts of the turret will rotate. If `rotPitch` = `true` then the animation will follow the up and down rotation of the turret. If `rotPitch` = `false` then the animation will follow the left and right rotation of the model.*

# Stat Graphs

## Data

Stat graphs are a way to define a property based on a current input stat. The graphs are 2D, so there are horizontal and vertical coordinates.
These coordinates are also known as Keys and Values.

For example, if one to look at an [Air Density Graph](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/air_density_minecraft_overworld.json),
the vehicle's current Y coordinate is the key, and the air density related to that Y coordinate is the value.
For example, in the Overworld Air Density Graph, a Y coordinate of 64 (Key) has an air density of 1.225 (value).

**Keys must be defined/sorted from least to greatest. The sorting of the values does not matter. The sorting must be done by Key.**

**Most Stat Graphs require a Key of Zero to be defined.**

If an input key is in between 2 defined keys, then the output value is linearly interpolated between the 2 values.
If an input key is above the highest key, then the value associated with the highest key will be returned.
If an input key is below the highest key, then the value associated with the lowest key will be returned.

All Stat Graphs have the following parameters:

`mirror_negative_keys` | BOOLEAN | **false** | *If `true`, input keys below zero will return the value associated with the positive input key.*

`invert_mirrored_values` | BOOLEAN | **false** | *If `true`, input keys below zero will return the negative value associated with the positive input key.*

### Available Preset Types

- `floatfloat`
- `aoaliftk`
- `floatfloat_multi`
- `turn_rates_speed`

### `floatfloat` Parameters

[Example graph using the `keys` and `values` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/air_density_minecraft_overworld.json)

[Example graph using the `map` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/alexis_drag_aoa.json)

`keys` | NUMBER_ARRAY | **OPTIONAL** | *A list of Keys. Must be the same length of `size` if used. An alternative way to create a graph is with `map`.*

`values` | NUMBER_ARRAY | **OPTIONAL** | *A list of Values. Must be the same length of `size` if used. An alternative way to create a graph is with `map`.*

`size` | NUMBER | **OPTIONAL** | *The length of the graph. REQUIRED IF defining the graph with the `keys` and `values` method.*

`map` | JSON_OBJECT_ARRAY | **OPTIONAL** | *An array of Json Objects where each entry contains a `key` and `value` property.*

### `aoaliftk` Parameters

This graph type encodes the Lift Coefficient associated with Angle of Attack.

These graphs use the same properties as `floatfloat`.

[Example graph using the `keys` and `values` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/fuselage.json)

[Example graph using the `map` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/javi_lift_aoa.json)

### `floatfloat_multi` Parameters

This graph type is similar to `floatfloat`, but only the `keys` and `values` method is available.
However `values` is a JSON_ARRAY of more NUMBER_ARRAYs containing the values.
This is because each key is associated with multiple values determined by `rows`.

`size` | NUMBER | **REQUIRED** | *The length of the graph.*

`rows` | NUMBER | **REQUIRED** | *The length of the graph.*

`keys` | NUMBER_ARRAY | **REQUIRED** | *A list of Keys. Must be the same length of `size` if used.*

`values` | JSON_ARRAY | **REQUIRED** | *A list of NUMBER_ARRAYs. Must be the same length of `rows` if used. Each sub NUMBER_ARRAY must be the length of `size`.*

### `turn_rates_speed` Parameters

This graph encodes the maximum turn rate for each rotational axis based on the current speed in `meters/tick`.
The number of rows must be 3. Where Row 1 is Pitch, Row 2 is Yaw, and Row 3 is Roll.
These turn rates are used when the `dscombat:planeArcadeMode` gamerule is set to `true`, and when `turn_assist` is enabled.

These graphs use the same properties as `floatfloat_multi`.

[Example graph.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/e3sentry_turn_rates.json)

# Models

The `obj_model_file` and `mlt_file` are required to add custom models to a resource pack. Vehicles, Weapons, and Parts all use Obj Models. There are special conventions that you must follow for each file to work. `global_model_transforms_file` is used for scaling and changing the model's pivot point. All three of these files must have the same name: `model_id`!

**`obj_model_file` = //assets/[`namespace`]/models/entity/[`model_id`].obj**

- All bones/groups/objects within the model must have unique names!
- The model must be facing towards POSITIVE Z! In minecraft and blockbench this is south, or towards the blue arrow!

**`mlt_file` = //assets/[`namespace`]/models/entity/[`model_id`].mlt**

- The `map_Kd` line must follow the resource location format to define the texture location. For example: `map_Kd dscombat:entity/vehicle/alexis_plane/base0`

**`global_model_transforms_file` = //assets/[`namespace`]/models/entity/[`model_id`].json**

- `scale` | NUMBER | **1** | *Scales the entire model in game.*
- `scalex` | NUMBER | **1** | *Scales the entire model in game on the X axis only.*
- `scaley` | NUMBER | **1** | *Scales the entire model in game on the Y axis only.*
- `scalex` | NUMBER | **1** | *Scales the entire model in game on the Z axis only.*
- `translatex` | NUMBER | **0** | *Translates the entire model on the X axis.*
- `translatey` | NUMBER | **0** | *Translates the entire model on the Y axis.*
- `translatez` | NUMBER | **0** | *Translates the entire model on the Z axis.*
- `rotationx` | NUMBER | **0** | *Rotates the entire model on the X axis in degrees.*
- `rotationy` | NUMBER | **0** | *Rotates the entire model on the Y axis in degrees.*
- `rotationz` | NUMBER | **0** | *Rotates the entire model on the Z axis in degrees.*