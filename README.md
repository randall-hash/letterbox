# Letterbox

Shrink the game into a small play area floating in black space, inside a maximized or fullscreen
RuneLite window. Built for the **Resizable – Classic** layout.

<!-- Add a screenshot here once captured: ![Letterbox in action](images/screenshot.png) -->

## What it does

In Resizable mode the game stretches to fill your whole window. On a large or ultrawide monitor
that can mean a lot of mouse travel and a sprawling interface. **Letterbox** renders the game at a
smaller size you choose and fills the surrounding space with black, giving you a compact, focused
play area centered on screen.

The game genuinely runs at the smaller size — the minimap, inventory, and orbs reflow to fit, and
clicking works exactly as normal. It is **not** a zoom, a crop, or an overlay.

## Why use it

- A smaller, focused play area on a big or ultrawide monitor — less eye and mouse travel.
- The black surroundings cut down on visual distraction.
- Fully configurable size, adjustable live.

## How to use

1. Install and enable **Letterbox** from the plugin list.
2. Set the game to **Resizable – Classic** (in-game Settings → Display).
3. Maximize or fullscreen the RuneLite window.
4. Open the Letterbox settings and set **Target width** and **Target height** to the play-area size
   you want.

The play area resizes live as you change the values, and stays correctly aligned when you open side
panels or resize the window.

## Settings

| Setting | Default | Description |
| --- | --- | --- |
| Target width | 1200 | Width in pixels of the play area. Clamped to the window size. |
| Target height | 800 | Height in pixels of the play area. Clamped to the window size. |

## Notes & compatibility

- Designed for **Resizable – Classic**. Fixed mode is unaffected; other resizable layouts may vary.
- Compatible with the **GPU** plugin.
- Disabling the plugin restores the game to fill the window.

## How it works (for the curious)

RuneLite's component tree is `ClientPanel` → game engine (`canvas.getParent()`) → `Canvas`. The
engine sizes itself from its **own** bounds and paints the canvas to fill it, so resizing the canvas
alone only crops the frame. Letterbox resizes the **engine component** inside the `ClientPanel`,
centered, letting the panel's existing black background show in the margins — which makes the game
reflow its UI and keeps clicks aligned, because the engine genuinely *is* that size.

One subtlety: the injected client refreshes its mouse→canvas mapping only on a genuine resize, not
on a move. So when the engine merely shifts (a side panel toggling, the window resizing), Letterbox
forces a 1px resize and debounces a final re-apply once the window settles, keeping clicks accurate.

## Building / running the dev client

```
./gradlew run
```

Launches a RuneLite development client with the plugin loaded. Logging in to a dev client with a
Jagex account requires writing launcher credentials once — see
[Using Jagex Accounts](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts)
(`--insecure-write-credentials`).

## License

BSD 2-Clause. See [LICENSE](LICENSE).
