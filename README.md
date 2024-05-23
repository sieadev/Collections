## PlaceholderAPI

### General data regarding collections
| Key                             | Data                               |
|---------------------------------|------------------------------------|
| `%Collection_{id}%`             | The collections name               |
| `%Collection_{id}_name%`        | -                                  |
| `%Collection_{id}_description%` | The collections description        |
| `%Collection_{id}_type%`        | The collections type               |
| `%Collection_{id}_target%`      | The collections target (mob/block) |
| `%Collection_{id}_levelSize%`   | Amount of Levels in the collection |

### Player specific scores regarding collections
| Key                              | Data                                                          |
|----------------------------------|---------------------------------------------------------------|
| `%Collection_{id}_level%`        | The players Level in the collection                           |
| `%Collection_{id}_nextLevel%`    | The players next level in the collection                      |
| `%Collection_{id}_score%`        | The players score in the collection                           |
| `%Collection_{id}_scoreToNext%`  | The amount of points the player is missing for the next level |
| `%Collection_{id}_bar%`          | Returns a styled bar showing the players progression          |

### Other Data regarding players
| Key                             | Data                                                           |
|---------------------------------|----------------------------------------------------------------|
| `%Collection_recent%`           | The collection the player increased their score in most recent |