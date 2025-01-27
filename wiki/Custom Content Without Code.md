This page is updated for v0.12.5. If anything is unclear please let me know in the discord so I can update this page!

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

`presetId` | STRING | **REQUIRED** | *MUST BE UNIQUE AND EQUAL TO THE JSON FILE NAME.*

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
- `max_speed` | NUMBER | **0.1** | *The maximum horizontal speed. Must be positive!*
- `mass` | NUMBER | **1000** | *Determined the vehicle's weight. Must be positive!*
- `stealth` | NUMBER | **1** | *A stealth value of 0 means the vehicle is invisible to radars. 1 means no stealth. Values greater than 1 make it easier for radars to see this vehicle. Values less than 1 make it harder for radars to see. Must be positive!*
- `cross_sec_area` | NUMBER | **10** | *Larger values mean more air resistance and make it easier for a radar to detect. Must be positive!*
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
- `max_spin_thrust_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific spin thrust per engine. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `heat_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific engine heat. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `fuel_consume_per_engine` | NUMBER | **OPTIONAL** | *Override the engine item stats with vehicle specific fuel consumed per tick per engine. Use `onlyCompatPart` in `slots` to require a specific engine.*
- `plane` | JSON_OBJECT | **REQUIRED FOR PLANES** | *Stats that only planes used are stored here.*
  - `flapsAOABias` | NUMBER | **8** | *If the plane's flaps are down, the plane's AOA is increased by this value in degrees. Some planes need an AOA greater than zero to take off, so they need their flaps to be down.*
  - `canAimDown` | BOOLEAN | **false** | *If true, a plane can press the Special 2 key to point the nose gun down about 25 degrees.*
  - `wing_area` | NUMBER | **10** | *Surface area of the plane's wings. A higher value means more lift generated from the wings. Must be positive!*
  - `fuselage_lift_area` | NUMBER | **0** | *Surface area of the plane's fuselage. A higher value means more lift generated from the fuselage. Must be positive!*
  - `wing_lift_k_graph` | STRING | **fuselage** | *A stat graph id. Must be a stat graph with a `presetType` of `aoaliftk`. [Stat Graphs that come with the mod.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/data/dscombat/stat_graph) Determines the lift coefficient for the wings based on AOA.*
  - `fuselage_lift_k_graph` | STRING | **fuselage** | *A stat graph id. Must be a stat graph with a `presetType` of `aoaliftk`. [Stat Graphs that come with the mod.](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/main/resources/data/dscombat/stat_graph) Determines the lift coefficient for the fuselage based on AOA.*
  - `wing_lift_hitbox_names` | STRING_ARRAY | **OPTIONAL** | *A list of names from `hitboxes`. The percentage of hitboxes in this list that are still alive, is the percentage of `wing_area` used to generate lift.*
  - `aoa_drag_factor` | NUMBER | **1** | *Scale how much drag high AOA causes.*
  - `centripetal_scale` | NUMBER | **0.4** | *Scale the horizontal force wings generate.*
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
- `anti_radar_missile`: These missiles target radar sources.

### All Weapons Parameter List

`maxAge` | NUMBER | **0** | *The max age in ticks before the weapon entity despawns.*

`fireRate` | NUMBER | **0** | *The length of cooldown in ticks before another weapon can be fired.*

`canShootOnGround` | BOOLEAN | **false** | *If true, this weapon can be shot while the owner vehicle is on the ground.*

`entityTypeKey` | RESOURCE_LOCATION | **OPTIONAL** | *The entity id that this wepaon uses. DO NOT OVERRIDE unless you coded a custom entity type!*

`shootSoundKey` | RESOURCE_LOCATION | **dscombat:bullet_shoot_1** | *The sound that plays when this weapon is fired.*

`compatibleWeaponPart` | STRING_ARRAY | **OPTIONAL** | *A list of Weapon Parts Ids and/or Turret Ids. These are the "guns" that this weapon is compatible with. [See here for a list of all parts!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/parts)*

`itemKey` | RESOURCE_LOCATION | **dscombat:ammo** | *Can also be set to `dscombat:bullet`, `dscombat:bomb`, or `dscombat:missile`.*

`modelId` | STRING | **`presetId`** | *The file name of the model that this weapon uses. Scroll down to the Models section for more info.*

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
 
# Radars

WIP. [Please see these examples for now!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/radars)

# Parts

WIP. [Please see these examples for now!](https://github.com/1whohears/DiamondStarCombat/tree/1.19.2-dev/src/generated/resources/data/dscombat/parts)

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