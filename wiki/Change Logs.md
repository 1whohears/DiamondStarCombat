# V0.14.3 beta | Dec 24, 2025

- Fixed incorrect Entity Renderer Registry call placement in forge. Should fix a lot of strange incompatibility issues.
- Fixed Eden Plane screen placement
- Increased Krait Chopper 3rd person camera distance

# V0.14.2 beta | Dec 20, 2025

- Fixed bug introduced in 0.14.1 where planes stopped accelerating fast enough to take off
- Corrected drag scale vs IRL scale calculation
- increased Bronco Plane speed

# V0.14.1 beta | Dec 17, 2025

- Added Fabric Support!
- Added the Willy Jeep! Model by **georkous**
- Added **masterofgamzz** vehicle workbench model
- Fixed not being able to drive backwards
- Eden Plane Model Fixes
- Fixed Spraycan not being accessible in creative mode tab
- Fixed certain physics constants not updating based on server configuration
- Fixed custom sounds from data packs not working

# V0.14.0 beta | Dec 10, 2025

**__Additions__**

- 1.20.1 is now the new main development version!

**__Changes__**

- All planes have been significantly buffed. The goal is to make every plane controllable. Before only the jets were good. Further turn rate balancing will be done in a future update.
    - At the default speed scale of 1/8th, all default presets will reach take off speed in 60 blocks or less at sea level.
    - If you try to take off at higher altitudes, it may require longer run ways.
    - Higher speed scales will require longer run ways.
- Some performance changes are currently being made for radars. Trying to get the client to do radar ray casting. Expect further radar mechanic changes in the future.

**__Fixes__**

- Fixed vehicle models not being properly shaded when rotating upside down.
- Fixed gun turret AI being very inaccurate at long ranges.
- Fixed bullets not properly going through multiple layers of glass.
- Fixed "you have not selected a target" error leading to many explosion particles if you hold down right click.
- Fixed turret entity hitbox sizes all defaulting to small.
- Somewhat fixed players falling out of their planes when the server lags.
- Many many more...

# V0.13.1 beta | Sep 11, 2025

**__Additions__**

- @boobcat added some WIP improved vehicle HUD. It is not done yet but you can enable it in the `dscombat-client.toml` file. Scroll down and set `enableModernHUD = true`. Press F6 by default to move the modern HUD elements around.
- Added Track Radar Target Camera mode. Press Right Shift by default to toggle it. It is very useful when flying with a controller.
- The IRL speed scale is now configurable in the server config file
- Added External Fuel Tanks
- Will now display the plane's thrust to weight ratio in the Heath vehicle menu.

**__Changes__**

- Helicopter physics have now been significantly improved thanks to @boobcat
- Increased max fall speed
- Changed the `/missile` command
- Significantly increase ground take off speeds for planes to make it easier to take off
- Seats will now also explode when a vehicle crashes because I hate you
- Missile speeds are now dependent on the IRL scale
- Air radar ranges are now dependent on the IRL scale
- RWR pings get smaller when they are farther away
- Anti Radar missile (agm88g) scan range is now based on universalSpeedScale. At 1/8th scale it has a seeker range of about 5K

**_Fixes_**

- Plane nose no longer gets stuck pointed up when reaching max altitude
- Fixed pilots hearing their own sonic booms
- Planes will try to verify if the chunk ahead of them is loaded to reduce the consequences of lag
- Fixed dog fight minigame spamming forfeit message
- Fixed secondary joystick not being plugged in causing throttle issues
- Fixed keybind overlay not updating when controller buttons are used

# V0.13.0 beta | Aug 9, 2025

## General Notes

One of the overall goals of the following changes is to try to make speeds, and other distant measurements __**1/8th**__ scale of their real life counter part. For example, the real life top speed of the F-16 is 605m/s, in DSC the top speed is 75.6m/s. The speeds of the fighter jets are roughly what they should be. However things like radar and missile ranges are not. Those will be fine tuned in future updates.

Another goal is to add energy management to dog fights. Real life dog fights are a dance of balancing speed and turn rate. Higher turn rates come at a cost of increasing drag, which rapidly reduces speed. Lower speeds reduce manoeuvrability. So finding a middle ground to have a sustained turn rate is important. But pilots will often "cash in"/sacrifice some of their speed for a short boost in turn rate when they can make the finishing shot. This has now roughly been implemented.

Because "pulling too hard" on the stick can cause turn rates to be too high -> increased aoa -> increased drag -> reduced speed, being more precise with your inputs is very import. This cannot be accomplished with digital keyboard inputs. Which is why I added controller support! See Vehicle Menu (U) -> Other Settings -> Edit Button/Joystick Binds.

It should be noted that the Alexis Plane was the primary test vehicle. I have not calibrated the other jets to the same degree, so they may not feel as good. Getting the Alexis to its current state was a lot of effort, and I want to make it feel as good as possible before I start intensely calibrating everything else. __So again, this release is not supposed to be perfect, but it is now finally in a presentable state. Your feedback is much appreciated.__

## Additions

- Added Afterburner toggle for fighter jets.
    - When afterburner is OFF, jets can reach their "cruise speed"
    - When afterburner is ON, jets can reach their much faster new top speed and consume x4 fuel.
- Added Turn Assist (AOA Limiter) toggle for modern aircraft. If your AOA gets too high, it will limit your turn rate. This significantly reduces the risk of stalling.
- Aircraft now simulate the forces from each lift surface.
    - Many properties like turn rate are now dynamic/emergent. For example, control surfaces like elevators can generate more torque when the plane is going faster.
    - If a wing gets destroyed, that lift surface gets lost, causing imbalanced torques.
- Fighter jets can now use Air Breaks.
- Attaching missile racks to wings will decrease a jet's manoeuvrability.
- Each dimension has a different air density graph that can be modified via datapacks. For example, the nether has a higher air density than the overworld.
- Added Controller support. To setup, open the Vehicle Menu (U) -> Other Settings -> Edit Button/Joystick Binds
- Added a Wind Tunnel entity to help with testing forces on an aircraft. Use `/summon dscombat:wind_tunnel` and `/windtunnel ...` commands.
- Render Distant player will now render distant Missiles and AI controlled vehicles.
- Added a basic sonic boom effect system. It is still buggy and needs more work, but it's too cool to leave out.

## Changes

- Thrust/Weight ratios have been properly increased (>1) to allow for vertical loop maneuvers.
- All Fighter Jets and missiles are now simulated using their real life Mass.
- Missile rack weight is now the sum of all the individual missiles it carries weights.
- Planes will now explode when they collide with water at high speed.
- Air to Air (FOX-1/2/3) Missile max ranges and top speeds have been significantly increased to match the 1/8th real life goal.
    - *A lot of work is left to be done to make their performance more realistic.*
- Air Radar ranges have also been significantly increased across the board.
    - *Improved stealth mechanics and distinguishing a radar contact from a trackable lock are on the way. In real life, just because a radar can "see" something at a long range, doesn't mean the track is accurate enough to be able to shoot a missile at it.*
- The RWR screen's display radius has been increased to 4000. Start performing defensive maneuvers if a red missile warning is inside of this range!
- Entities can only been seen from air radars if they are at least 6 blocks above the ground.
- All DSC gamerules now begin with `dscombat:` to make them easier to find while tabbing through options in commands.
- Removed an internal slot from the Alexis plane. Reduces potential fuel capacity. *Need to add external tanks.*
- Added universal limit to helicopter climb speed (20m/s).
- Turret AIs will now wait for the previously fired missile to die before shooting another one.

## Fixes

- Fixed Aircraft speeds not properly saving/getting loaded on client. If a player is still in a plane when the server restarts or when the player re-logs, the plane should keep its original velocity.
- The `dscombat:weaponsBreakBlocks` will disable block breaking explosions from missiles if set to false.
- Fixed radar missile turret AI randomly stop shooting.
- Fixed/improved the Relative Camera mode. 3rd person camera will stay in the same relative position even when the plane rolls.
- Fixed planes not taking any damage while sliding on the ground.
- Fixed ammo reload screen not working for turrets.
- Fixed only the IR high tone playing for fox 2s. Now when there is no IR target visible the low tone will play.

# V0.12.9 beta | Apr 18, 2025

- added support for the soon to be released render distant players mod

# V0.12.8 beta | Mar 22, 2025

- fixed sounds breaking if game is run for a while
- fixed clients overriding speed factor
- nerfed tank health
- buffed wheel recipe

# V0.12.7 beta | Mar 1, 2025

- added village defense kits/shops for custom minigames
- weapon entities now use the 1wholibs custom anims obj model system
- added dumb_torpedo
- can configure ping overlay size in vehicle gui
- changed ping overlay colors
- added creative turret platform

# V0.12.5 beta | Feb 5, 2025

- Fixed mr budget tank recipe
- Fixed vehicles trampling claimed chunks immediately after dismount
- Can now add custom passenger voice packs with resource packs
- Added dogfight and village defense game modes through the optional Custom Mini Games mod

# V0.12.4 beta | Jan 21, 2025

- fixed newly crafted parts not working..oops..
- fixed custom vehicle name getting set to nothing
- fixed owner_id not saving when the vehicle is left out during saves
- ammo items now use custom item renderer
- fixed vehicles, turrets, and ammo not appearing in creative/jei search

# V0.12.3 beta | Jan 17, 2025

- Fixed Botania incompatibility
- All vehicle parts now use data generators including turrets and external weapon racks. This allows addon devs to make custom turrets and missile racks without code!
- Fixed client server de-sync issues with ammo and fuel.
- Only pilots can turn vehicles into items.
- Fixed weapon rack/turret reload crafting recipe
- Fixed negative durability wrench lmao
- Fixed gas can duplication glitch
- Fixed getting dismounted into the void
- Normalized vehicle, turret, and weapon rack items (dscombat:vehicle, dscombat:turret)
- Vehicle and turret items use a custom item renderer so the actual models get rendered in the item.
- Fixed owner not getting saved when vehicle is first turned into an item
- FTBTeams and OpenPAC are now optional dependencies. The radar/perms system checks if players are allied through these modded team systems.
- Added the Ticket Book: right click vehicle, then shift right click villager. The villager will walk to the vehicle and sit in an open passenger seat.
- Turrets and External Weapon Parts are now crafted through the Weapon Parts Workbench
- Added canVehicleItemWhileMoving gamerule. false by default

# V0.12.1 beta | Dec 14, 2024

- Nerfed custom villager trades
- Added a few more compatibility tags

# V0.12.0 beta | Nov 21, 2024

NEW VEHICLES
- Added Eric Truck. Model by blackbirdantlion
- Improved Mr Budger Tank model by blackbirdantlion
- Added MLRS anti air turret. Model by blackbirdantlion
- Added Artillery Cannon. Model by blackbirdantlion
- Added Early Warning Radar 4000 (ewr4000). Model by CEO of Goolge. A stationary radar station that runs as long as the chunk is loaded.

OTHER
- Added a new vehicle menu with a bunch of different sub menus. By default press U to open it.
- Added vehicle health UI. See how much health the root and the individual parts in the vehicle have. Also see how much fuel the vehicle has.
- Improved vehicle storage UI.
- Added vehicle weapon system UI. Select the weapon and set target coordinates for position guided missiles
- Added vehicle radar system UI. Set the radar mode and display range.
- Added weapon reload and unload UI.
- Added Jetesin external parts UI.
- Added vehicle permission system. Cycle between PUBLIC, ALLIES, and PRIVATE. For example, if set to ALLIES, only team members can ride your vehicle.
- Added a bunch of new vehicle screens (Attitude, Altimeter, AOA meter, Turn coordinator, big radar)
- Removed the fuel, turn coordinator, and attitude indicator overlays. They have been moved to the in game vehicle screens.
- Added lean left/right keybinds. They are unbound by default.
- Radars can now see missiles. However currently, you can't shoot at missiles with radar missiles yet.

# V0.11.2 beta | Oct 18, 2024

- Added Eject key "]"
- Can craft an eject seat (seat + parachute + gunpowder)
- Vehicles can trample certain blocks (check vehicle trample tag) can be disabled with a gamerule
- Added vehicle speed factor to common config
- Dimension sea level/air pressures are configurable
- Updated 1wholibs, advanced keyframe animations can now be added.
- the cwis barrel now spins. more advanced animations will come later.
- added `/targetmode` command:
- `/targetmode look` position guided missiles will go to where the player/gimbal looks
- `/targetmode coords <pos>` position guided missiles will go to the given coords

# V0.11.1 beta | Sep 2, 2024

- plane turn rates are now based on turn rate vs air speed graphs
- fixed server and asset reloads not updating stats/models from content packs
- added max altitude stat for planes
- added removeDeadVehiclesTime gamerule
- fixed kill command not working for vehicles with hitboxes
- fixed pillager radar missile turrets not working for vehicles with hitboxes
- fixed missiles getting deleted by owners hitboxes on the client side
- fixed 3rd person mirrored camera incorrect pitch angles
- bullets break weak blocks
- weapons breaking blocks fires an event to ensure they have permission to do so

# V0.11.0 beta | Aug 23, 2024

**BACKUP YOUR WORLDS!!!
OLD VEHICLE ENTITIES MAY GET DELETED!!!
BACKUP YOUR WORLDS!!!**

**onewholibs IS NOW A REQUIRED DEPENDENCY!!!**

- Added a simple plane physics mode! `/gamerule planeArcadeMode true`
- Added the Krait Chopper (Model by rainsky_0431)
- Added the James Wooden Plane (Model by pretzelpenguin777)
- Added 5 more Eden Plane Skins by rainsky_0431
- Added new item textures by rainsky_0431
- Added new Parachute model by .ceoofgoogle
- Added some engine item models by .ceoofgoogle
- Added a rotary engine item model by pretzelpenguin777
- Added improved bullet models by .ceoofgoogle
- Added some shoot sounds by andryqx
- Added new ping data icons by blackbirdantlion
- Fixed oscillating/inconsistent AOA on certain planes
- Renamed aircraft_client asset folder to vehicle_client

# V0.10.0 beta | Aug 2, 2024

**BACKUP YOUR WORLDS!!!**
**OLD VEHICLE ENTITIES MAY GET DELETED!!!**
**BACKUP YOUR WORLDS!!!**

v0.10 is basically an under the hood overhaul. These changes make it possible to add custom vehicles/weapons with just data/asset packs! I will be updating the wiki and adding tutorials on how to do this. Yes this update doesn't have as much new "content", but adding new stuff should be a lot easier moving forward.

## Changes
- All vehicle/weapon entity ids have become generalized. Instead of a bunch of different plane entities (dscombat:alexis_plane,dscombat:javi_plane,dscombat:wooden_plane), they are now all registered under the same entity id (dscombat:plane,dscombat:car,dscombat:boat). This means vehicles spawned before v0.10 may not load due to those entity ids no longer existing. I attempted to add a "failsafe" system to account for this, but I can't guarantee it will always work!
- Overhauled the preset system. Nearly everything is customizable in json files. Will be much simpler to add custom preset types in addons. (useful for making custom weapon/vehicle types)
- All vehicle stats are data driven. Example: one could make a datapack that modifies alexis plane base stats, or adds a new preset with a custom weapon loadout.
- All weapon stats are data driven. Example: one can make a datapack that nerfs or buffs weapon stats.
- Vehicle Part Item stats are now controlled by json files. Example: one could make a datapack that increases the fuel capacity of a heavy fuel tank. The "preset id" of a part item is determined by its NBT.
- Slot types are now hierarchical. Simple to add custom slot types in addons.
- Max ammo is now determined by "weapon holder" instead of the weapon's own stats.
- Vehicle presets have an "assetId" that determine which Vehicle Client preset they use. The client presets control which model the plane uses, and its animations.
- Converted all vehicle models to obj models. Moving forward, ONLY OBJ models will be used!
- Weapon codes now match IRL call outs. (FOX2, FOX3)
- Changed some default custom game rule values

## New Stuff
- New vehicle hit sounds and weapon shoot sounds - kawaiicakes
- New vehicle damaged particles - kawaiicakes
- Added light/med/heavy pylon and light/med/heavy mount and internal gun slot types.
- The fighter jets have unique internal guns.
- Weapons can now be compatible with more than one weapon/turret.
- A client side data driven animation system for vehicles. The goal is to get rid of hard coded animations within vehicles.
- Part Items can now get damaged when the vehicle gets damaged. To repair them you must combine them with a wrench in a crafting table.
- Vehicle Armor system. Function similar to shields in fortnite.
- Fighter jets have custom hitboxes. You can cut the wings off, or blow up the engine.
- Dead vehicles will not despawn until you right click them to recover their parts.
- Fighter jets can now roll 90 degrees to do realistic turns at high speeds.
- The 127mm bullet now makes a small explosion

## Bug Fixes
- Fixed some of the weapon ammo item mixing issues
- Jason plane now tilts up when on the ground
- Fixed player and hitbox being in different chunks breaking rotable hitboxes
- Walking on hitboxes is a bit less buggy in general
- Fixed Boat Jail

# V0.9.9 beta | Apr 29, 2024

- Distance external parts are rendered can be changed in client config
- Planes get damaged if they scrape across the ground without landing gear
- Passengers that jump out of the vehicle shouldn't get launched anymore
- Somewhat fixed the wooden/Jason plane unstable AOA
- Fixed spray can putting player in the vehicle
- Added some new creative mode wands: Instant Repair, Auto Refill. Right click vehicles with them.
- Show what parts weapons are compatible with in the weapons workbench
- Fixed a lot of turret AI bugs
- Data link should hopefully work this time
- Non pilot passengers can now change the radar mode
- Size of overlay radar pings can be changed in client config
- The distance on the radar overlay turns green if the target is within the radar missile's maximum theoretical range
- Many other bug fixes

# V0.9.8 beta | Apr 21, 2024

- Vehicle spins in workbench GUI
- Added Ant's updated weapon overlay textures
- Fixed plane rotation de-syncs
- Gas can mending crash hotfix
- Turrets can be compatible with more than one weapon
- Slight adjustments to vehicle recipes
- Loads of other bug fixes

# V0.9.7 beta | Apr 14, 2024

- New Chain Hook System! Attach a chain Hook to parent vehicle. Right click Chain Hook and Child vehicle with a chain to connect them!
- Reworked fuel system. OIL!
- Vehicle and Weapon Recipes are now compatible with tags!
- Use tags in a lot of other places to improve compatibility.
- Lots of bug fixes.
- Chains should work on servers.

# V0.9.5 beta | Mar 13, 2024

- The max number of missiles that can be rendered under missile racks can be changed in client config. Rendering a lot of missiles under the racks kills performance for some reason.
- Fixed Aircraft and Weapon Workbench not showing the number of items needed.
- Fixed Armor piece recipe.
- Added Aluminum. A cheap metal used to make all the aircraft parts. Makes everything much cheaper for survival.
- Added JEI integration.
- Fixed AGM-84E not working.
- Fixed stats all being zero in the aircraft workbench.
- Added some new bomb models from PretzelPenguin
- Reduced explode smoke radius.

# V0.9.4 beta | Mar 9, 2024

- Renamed Google's Boats
- Missile Racks render the actual missile models
- Vehicle screens will now be defined by a screen texture map
- Fixed vehicle screen text being visible through walls
- Added heading and air speed screen
- Added new Meteor missile, AGM-88G missile, and bomb models

# V0.9.3 beta | Feb 24, 2024

**REVIEW v0.9.2 CHANGE LOG**

**BACKUP WORLDS BEFORE USE**

**Additions**
- Added the Eden Plane! A 2 seat fighter jet! The model was created by rainsky_0431
- Added the Google Sub! A giant submarine. The model was created by .ceoofgoogle
- Added cockpit voice lines for the fighter jets ("Stall Alert", "Engine Fire"). Check out the client config to configure it.
- Added an Aim Assist overlay for bullets and bombs. Points to where your bomb would land.
- Overhauled the Alexis Plane's sound effects! Better sounds will come to other vehicles soon.
- Added a vehicle Storage Box item. Basically a shulker box for vehicles.
- Sounds are delayed due to the speed of sound.
- Added Blend Mode setting for paint job layer colors

**Changes**
- Made the keybinds overlay a bit less ugly
- Adjusted air to air radar missile stats. When they run out of fuel they loose speed when they turn.
- Vehicle preset json files are a bit more organized now.
- Child presets can now inherit data from their parents. This significantly shortens the size of many of the vehicle preset json files.
- Custom hitboxes are now defined in the json files.
- Vehicle Screen data are now defined in the json files.
- Slightly changed the plane lift force direction to be more realistic. In real life the lift force direction is perpendicular to air flow. Before it was perpendicular to the wings which allowed infinite flying if the nose was pointed down a bit.
- The rgm84 missile can now hit ground and water targets.
- Planes will loose %30 of their max speed when throttle is below %50. This will allow for easier speed control.
- Certain missiles and bombs will now explode twice. The strat of just hiding behind one block of dirt won't work anymore.

**Fixes**
- Turret Ammo will display in the bottom left like other weapons
- Fixed "bots" radar scan mode not being able to see individual players
- Passenger looping sound won't "stutter" anymore
- Weapon entities won't immediately despawn when they get summoned anymore.
- part data stats are now synched based on their respective item. for example this means when engine item stats get changed in future versions, instances of that engine made in previous versions will also get updated.
- fixed flare key only working if it is held
- after burner particles should now glow
- Anti Radar missiles won't track vehicles that have their radar turned off
- Fixed vehicles not appearing in creative search
- Fixed submarine not floating underwater
- `EntityVehicle#setYRot` and `EntityVehicle#setXRot` now work as expected
- Fixed rgm84 not working on the destroyer
- Fixed vehicle radio volume issues
- Significant performance boost to vehicle layer texture rendering
- Removed ammo dupe glitch and fixed the reload recipes

# V0.9.2 beta | Jan 9, 2024

**REVIEW v0.9.1 CHANGE LOG**

**BACKUP WORLDS BEFORE USE**

**Additions**

- Gave Javi plane a new texture - rainsky_0431
- Added BOTS radar mode which shows players and turret AI mobs

**Changes**

- Adjusted Boat fixes. Bouncy
- Adjusted Alexis bonus presets
- Changed flare particles

**Fixes**

- Fixed incompatibility with Botania
- Gave agm88g item a model
- Fixed players sometimes getting stuck inside boat hitbox
- Fixed boat platforms sometimes being "sticky" for players
- Fixed radar not seeing players in turrets when in players only mode
- Having multiple flare dispensers in a vehicle will shoot one at a time
- Somewhat reduced debug message spam in the console log

# V0.9.1 beta | Dec 26, 2023

**REVIEW v0.9.0 CHANGE LOG**

**BACKUP WORLDS BEFORE USE**

**Additions**

- Added Gimbal Camera part. Press ";" by default to view the world through the Gimbal's perspective. Useful for seeing enemies on the ground.
- Added some WIP after burner particles for planes.
- Vehicles below 50% health now have a chance of leaking fuel and/or engines catching fire. Right click with wrench to repair.
- Different weapon types now have their own symbols on the weapon select overlay.

**Changes**

- Overhauled a lot of physics constants. Adjusted the weights and thrust values of everything. All vehicles will "feel" different. Some may feel "heavier" than before. These physics values will be configurable once I find default values that I like.
- Adjusted the mouse stick mode. Should be slightly less annoying to use. Check out the client config to configure it.

**Fixes**

- Fixed vehicles filling up the game's sound limit.
- Fixed turn coordinator ball flying off the sensor.

# V0.9.0 beta | Dec 18, 2023

**BACKUP WORLD BEFORE UPDATING:** this version changes a lot and I can't guarantee all your old vehicles will function properly.

The mod is still in a very beta/alpha stage. But this version is a cool sneak peak of what is coming in the future.

**MODERN FIX IS NOW A REQUIRED DEPENDENCY:** There is a bug in forge where if a vehicle and player passenger are not in the same chunk the server sends tons of chunk update packets every tick causing horrible performance issues. Modern Fix fixes this. Because the player and vehicle are often not in the same chunk in this mod, this fix is desperately needed.

**Known Issues (FTSL)**

- Sometimes entities will fall off aircraft carriers and other boats when chunks are being loaded and unloaded. I don't know how to fix this yet. Don't leave any important vehicles/entities on the aircraft carrier until this is fixed. You have been warned. I am not responsible for any planes that teleport under the carrier and get destroyed lol.
  The transparent glass in plane canopies like the Alexis and Javi are not tinted from the inside. This is because when they were tinted players couldn't see other vehicles outside the cockpit through the glass. This is a temporary solution.
- The mouse mode is still garbage I'm sorry. It will get fixed eventually.
  Sometimes if the client sees too many vehicles at once, sound effects just stuff working. I have no idea why.
- I need to re-balance all the physics constants. Not all movement in this mod will "fell good" yet.
- For some reason vehicles don't show up in creative search.
- Yes the Jason plane should be tilted up when it's on the ground.

**New Vehicles/Models**

- Added Bronco Plane - rainsky_0431
- Updated Javi Plane Model - rainsky_0431
- Updated Alexis Model - rainsky_0431
- Added Felix Plane - rainsky_0431
- Added Jason Plane - rainsky_0431
- Added MK-13 torpedo model - pretzelpenguin777
- Added Radar Boat Masts - .ceoofgoogle

**New Features**

- In game vehicle screens. Radar/RWR/Fuel/HUD and more to come.
- New vehicle custom texture system. Right click vehicles with the spray can to change the base texture and layer textures. Want more vehicle skins and layers? Please contribute some!
- Boats have hitboxes that entities can walk on. When the boat moves, entities on the platform move.
- Hostile mobs and golems that ride turrets will shoot at players. They can shoot radar missiles out of the Axcel Truck too, not just guns. If the mobs are on the same team as you they wont shoot at you. Different mobs have different degrees of accuracy.
- WIP particle system for explosions and missile trails.
- Added Meteor Missile
- WIP Vehicle Hud overlay - kawaiicakes
- New Weapon Select Overlay - kawaiicakes
- Added new custom gamerules:
- Consume Fuel/Ammo/Flares
    - Auto Data Link
    - Disable Elytra
    - Vehicle to Item Cool Down
    - Can mobs ride vehicles
    - Plane speed percent multiplier gamerule.
    - Multiple damage multiplier gamerules.
    - Disable 3rd person while riding vehicle.
- Chat messages if a missile hits a target. Controlled by custom gamerule.
- Pilot 3rd person camera is now positioned farther to see whole vehicle.
- Added `/givevehicle` command to easily get a vehicle of a certain preset.

**Changes**

- Javi Plane is now single seat.
- Alexis Plane has 2 new hard points on the edges of wings.
- Changed missile stats. Subject to change in the future.
- New HUD compass that's not as laggy - kawaiicakes
- Added an additional keybind category.
- Added an additional creative item tab.
- Radar Ping Hud overlay will show more info about a target (is grounded, is player, is friendly ext...)
- Renamed rifel1 to agm88g
- Renamed torpedo1 to mk13
- Missile items are now their models
- There are now 3 camera/mouse modes: Free Relative, Free Global, and Fixed Forward.
- All vehicles now have a max climb speed of 15m/s (looking at you helicopters). Subject to change.

**Fixes**

- An incompatibility issue with Mr Crayfish Guns causing the camera to not roll when the plane rolls.
- Pings on the hud will continuously update position if target entity is in view distance instead of lagging behind.
- Entities will now only ride a vehicle if a seat hitbox touches it.
- Can't right click another seat to ride it while riding a seat.
- Vehicle items models not oriented correctly in item frames.
- Too many more to list here.