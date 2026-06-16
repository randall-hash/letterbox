# Current State — Letterbox

Session handoff. Read this first to resume.

## Where we are (2026-06-15)

**Working and confirmed in-game by Randall.** The plugin shrinks the game to a configurable size,
centered in black space, in Resizable – Classic. UI reflows correctly and clicks stay aligned —
including with the sidebar open/closed and after resizing the outer window.

Committed to `master`. Public GitHub repo + Plugin Hub submission are the next steps.

> Note: the local working directory is still named `viewport-shrink/` (the plugin was renamed to
> Letterbox after the folder was created). Harmless — it's just a folder name.

## What's done

- Renamed off the `com.example` template, then renamed again to **Letterbox**: package
  `com.letterbox`, classes `LetterboxPlugin` / `LetterboxConfig` / `LetterboxPluginTest`, config
  group `letterbox`, plus `build.gradle` / `settings.gradle` / `runelite-plugin.properties` /
  `logback-test.xml`.
- Core mechanism: resize the **game engine component** (`canvas.getParent()`), not the canvas, so
  the game reflows and hit-detection follows. Resizing the canvas alone only crops.
- Click-alignment fixes: 1px forced-resize on a reposition (client only refreshes its mouse mapping
  on a real resize, not a move) + a 200ms debounce so a window resize settles before the final
  re-apply.
- Config defaults: 1200×800. BSD-2 LICENSE, README, this handoff. Diagnostic logging removed.
- Self-audit passed (see below).

## Key technical insight (don't relearn this)

`client.getCanvas().getParent()` is the obfuscated game engine (class `client`). The engine renders
at its own size; the canvas is its child. The injected client refreshes mouse→canvas mapping only
on resize, never on a pure move — that's the root of every click-desync we hit.

## Self-audit result

Clean against the AGENTS.md guidelines: no reflection/JNI/Unsafe/processes/serialization, Java 11,
EDT-only AWT work with the Swing Timer stopped on shutDown, no input injection, no menu/server
actions, no network/file I/O, BSD-2 license, fully renamed off the template, no build artifacts
tracked. One gray area to disclose in the Hub PR: "no moving/resizing click zones for 3D
components" — we resize the whole viewport uniformly (smaller, no advantage; Stretched Mode is
precedent). Reviewer's call.

## Next steps

1. **Public GitHub repo** + push `master` (done this session if you see a remote). Plugin Hub builds
   from the public source by commit hash.
2. **Submit to the RuneLite Plugin Hub**: PR to `runelite/plugin-hub` adding a manifest file that
   points at the repo + commit hash, with the click-zone gray area noted in the PR description.

## How to test

`./gradlew run` from repo root → log in (Jagex dev credentials, see README) → Resizable – Classic,
maximize → enable **Letterbox**. Verify: reflow looks right; clicks land correctly with sidebar open
AND closed; clicks stay correct after resizing the outer window.
