# Dread Sovereign 0.1.0-alpha source archive

This directory stores the first-pass Dread Sovereign source ZIP in base64 segments because the GitHub connector used to create the prototype cannot upload binary files directly.

Reconstruct the source archive from the repository root:

```bash
cat prototype-mods/dread-sovereign-archive/source.zip.b64.* \
  | tr -d '\n' \
  | base64 -d \
  > DreadSovereign_0.1.0-alpha-source.zip
```

Verify it before extracting:

```bash
printf '%s  %s\n' \
  '0b0637f570c8cba2e4b2f53bb67ad28957996397c1d2afbb5069cd291abf1f45' \
  'DreadSovereign_0.1.0-alpha-source.zip' \
  | sha256sum -c -
```

Then extract:

```bash
unzip DreadSovereign_0.1.0-alpha-source.zip
```

The CI workflow reconstructs and verifies the same archive before attempting the Java 25 / NeoForge build.
