#!/bin/zsh

./gradlew build && docker compose build && docker compose up -d