# at the start of each round, remove the file and kill the jeeps
function dss:remove_fire_at_spawns
schedule function dss:summon_jeeps 10t
execute as @a run schedule function dss:give_spawn_vehicle_event 10t