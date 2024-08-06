# Custom Ender Dragon (Bukkit)
### A minecraft plugin that changes the final battle!
>Feel free to alter the code of this plugin to your needs

## Features
> [!TIP]
> Most of the features are customizable or could be disabled.

On dragon taking damage:
- 50% dragon to receive `Regeneration (III) 2s` & `Resistance (IV) 2s`
- 50% player to receive `Levitation (I) 3s` & `Blindness (I) 2s`
- 50% dragon to launch `Dragon Fireball`
- `red` particles around the dragon
- `fire` particles around the player
- `EXPERIENCE_ORB_PICKUP` sound for player
- `RAVAGER_HURT` sound for player

On dragon death:
- displays a leaderboard of `Top 5 damage dealers`

On end crystal break:
- `title` for all players in the end world
- `TOTEM_USE` sound for all players in the end world

Dragon abilities:
- repeated every `5s`
- dragon shoots every player with `5 arrows` and `1 Dragon Fireball `
- all players in `15x15x15` receive Poison (I) 10s & `Weakness (I) 1s`
- once fallen below `75%` health, receives `Strength (I)` & `Speed (I)`
- once fallen below `50%` health, receives `Strength (II)` & `Speed (II)`
- once fallen below `25%` health, receives `Strength (III)` & `Speed (III)`

## Installation
1. Download the latest release on the right side of the page.
2. Drop the `.jar` file in your `plugins` folder.
3. Start the server and a folder `CustomEnderDragon` will be generated with a `config.yml` file.
4. Configure the plugin using `config.yml` to your needs
5. Reload the plugin using `/ced reload`