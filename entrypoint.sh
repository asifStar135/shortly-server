#!/bin/sh

set -e

GEOIP_DIR="${GEOIP_DIR:-/app/geoip}"

mkdir -p "$GEOIP_DIR"

fetch() {
  curl -fsSL -u "${MAXMIND_ACCOUNT_ID}:${MAXMIND_LICENSE_KEY}" \
    "https://download.maxmind.com/geoip/databases/$1/download?suffix=tar.gz" \
    -o "/tmp/$1.tar.gz" \
  && tar -xzf "/tmp/$1.tar.gz" -C /tmp \
  && find /tmp -name "$1.mmdb" -exec mv {} "${GEOIP_DIR}/" \; \
  && rm -rf "/tmp/$1"*
}

if [ -n "$MAXMIND_ACCOUNT_ID" ] && [ -n "$MAXMIND_LICENSE_KEY" ]; then
  fetch GeoLite2-City    || echo "warn: GeoLite2-City fetch failed, continuing without it"
  fetch GeoLite2-Country || echo "warn: GeoLite2-Country fetch failed, continuing without it"
fi

exec java -jar app.jar --server.port="${PORT}"