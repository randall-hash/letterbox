# Viewport Shrink

A RuneLite plugin that renders the game at a smaller size than the client window and fills the
leftover space with black — a small play area floating in black space inside a maximized or
fullscreen RuneLite. Designed for the **Resizable – Classic** layout.

The game genuinely renders at the smaller size (the UI reflows and mouse hit-detection stay
correct) — it is not a crop or an overlay.

## Configuration

| Setting | Default | Description |
| --- | --- | --- |
| Target width | 1200 | Width in pixels of the play area. Clamped to the window size. |
| Target height | 800 | Height in pixels of the play area. Clamped to the window size. |

Config group: `viewport-shrink`.

## How it works

RuneLite's component tree is `ClientPanel` → game engine (`canvas.getParent()`) → `Canvas`. The
game engine sizes itself from its **own** bounds and paints the canvas to fill it, so resizing the
canvas alone only crops the rendered frame. To actually shrink the game we resize the **engine
component** inside the `ClientPanel`, centered, letting the panel's existing black background show
in the margins.

Two non-obvious details the implementation handles:

- **UI reflow** — resizing the engine (not the canvas) makes the game reflow minimap/inventory/orbs
  to the smaller size, and keeps clicks aligned because the engine genuinely *is* that size.
- **Click alignment on move** — the injected client refreshes its mouse→canvas mapping only on a
  genuine resize, not on a move. When the sidebar toggles or the window resizes, the engine shifts
  position while keeping its size, which would leave clicks off by the shift. The plugin forces a
  1px resize on a reposition, and debounces a final re-apply after a window resize settles.

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
