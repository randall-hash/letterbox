# Current State — Viewport Shrink

Session handoff. Read this first to resume.

## Where we are (2026-06-15)

**Working and confirmed in-game by Randall.** The plugin shrinks the game to a configurable size,
centered in black space, in Resizable – Classic. UI reflows correctly and clicks stay aligned —
including with the sidebar open/closed and after resizing the outer window.

Committed to `master`. Not yet pushed to GitHub; not yet submitted to the Plugin Hub.

## What's done

- Renamed off the `com.example` template → package `com.viewportshrink`, classes
  `ViewportShrinkPlugin` / `ViewportShrinkConfig` / `ViewportShrinkPluginTest`, config group
  `viewport-shrink`, updated `build.gradle` / `settings.gradle` / `runelite-plugin.properties`.
- Core mechanism: resize the **game engine component** (`canvas.getParent()`), not the canvas, so
  the game reflows and hit-detection follows. Resizing the canvas alone only crops.
- Click-alignment fixes: 1px forced-resize on a reposition (client only refreshes its mouse mapping
  on a real resize, not a move) + a 200ms debounce so a window resize settles before the final
  re-apply.
- Config defaults: 1200×800 (the size Randall liked while testing).
- Diagnostic logging removed; README + this handoff written.

## Key technical insight (don't relearn this)

`client.getCanvas().getParent()` is the obfuscated game engine (class `client`). The engine renders
at its own size; the canvas is its child. The injected client refreshes mouse→canvas mapping only
on resize, never on a pure move — that's the root of every click-desync we hit.

## Next steps

1. **Set up the public GitHub repo** and push `master` (Plugin Hub builds from source by commit
   hash). `gh` CLI auth status unverified — check before creating the remote.
2. **Submit to the RuneLite Plugin Hub**: PR to `runelite/plugin-hub` adding a manifest file that
   points at the repo + commit hash. A RuneLite dev reviews against the third-party guidelines.
3. **Compliance note to watch in review**: the guideline "no moving/resizing click zones for 3D
   components" targets *advantage*; we resize the whole viewport uniformly (smaller, no advantage),
   precedent being built-in Stretched Mode. Defensible but reviewer's call.

## How to test

`./gradlew run` from repo root → log in (Jagex dev credentials, see README) → Resizable – Classic,
maximize → enable **Viewport Shrink**. Verify: reflow looks right; clicks land correctly with
sidebar open AND closed; clicks stay correct after resizing the outer window.
