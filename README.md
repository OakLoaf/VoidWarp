![Banner](https://imgur.com/llldWYu.png)
# VoidWarp
This is at heart a simple void teleport plugin. However, it offers multiple different compatibilities and multiple different teleport modes. Most notably the ability to teleport the player to the nearest warp to the player!

Want to stop your players infinitely falling in your [Standalone Limbo](https://www.spigotmc.org/resources/limbo-standalone-server-lightweight-solution-for-afk-or-waiting-rooms-in-your-server-network.82468/)? Try [VoidWarp-Limbo](https://github.com/CoolDCB/VoidWarp-Limbo)!

**Download Link**: https://www.spigotmc.org/resources/voidwarp.107811/

<br/><br/>
![Features](https://imgur.com/ziDphRe.png)
- Multiple different teleport modes
- Unique teleport to the nearest warp feature
- Setup different modes in different worlds
- Can be used to block the nether roof from being used!
- Save your players from dying in the void

<br/><br/>
![Config](https://imgur.com/UUHchLd.png)
```yaml
############################
#     VoidWarp - Config    #
############################

world:
  # mode: The mode to use when the player is in the region (SPAWN/COMMAND/WARP/ESSENTIALS_WARP/HUSKHOME_WARP)
  mode: SPAWN
  # yMin: The Y coordinate at the bottom of the range (Leave empty to have no minimum)
  yMin: -1000
  # yMax: The Y coordinate at the top of the range (Leave empty to have no maximum)
  yMax: 0
  # message: The message that is displayed in chat when you are teleported
  message: "<grey>Teleported to <yellow>%location%</yellow>."
  # commands: Commands to be ran when in the region (Note: Only valid if mode is "COMMAND") (COMMANDS CURRENTLY ONLY RAN AS PLAYER)
  commands: [ ]
  # warps: A list of warps (Note: Only valid if mode is "WARP", "ESSENTIALS_WARP" OR "HUSKHOME_WARP")
  warps: [ ]
  # whitelist: Sets whether the warps list is a whitelist or blacklist (Note: Only valid if mode is "WARP")
  whitelist: true
```

If you have a bug to report or need help setting up your plugin join the [Discord Support Server](https://discord.gg/p3duRZsZ2f)


Have a wonderful day :)
