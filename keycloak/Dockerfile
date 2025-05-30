FROM quay.io/keycloak/keycloak:26.1.4 AS builder

ENV KC_HOSTNAME_STRICT_BACKCHANNEL=true
ENV KC_HTTP_RELATIVE_PATH=/
ENV KC_HTTP_ENABLED=true
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

ENV KC_DB=postgres

WORKDIR /opt/keycloak
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:26.1.4
COPY --from=builder /opt/keycloak/ /opt/keycloak/
COPY realm-config.json /opt/keycloak/data/import/realm-config.json

ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://postgres:5432/polar_keycloak
ENV KC_DB_USERNAME=username
ENV KC_DB_PASSWORD=password

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]