/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.api.controllers;

import com.microfocus.example.payload.response.ApiStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A RESTFul controller for accessing site information.
 *
 * @author Kevin A. Lee
 */
@RestController
@RequestMapping(value = "/api/v3/site")
@Tag(name = "site", description = "Site operations")
public class ApiSiteController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApiSiteController.class);

    public class SiteStatus {
        private String health;
        private String motd;

        SiteStatus() { }
        SiteStatus(String health, String motd) {
            this.health = health;
            this.motd = motd;
        }

        public String getHealth() {
            return health;
        }
        public void setHealth(String health) {
            this.health = health;
        }
        public String getMotd() {
            return motd;
        }
        public void setMotd(String motd) {
            this.motd = motd;
        }
    }

    @Operation(summary = "Get the site status", description = "Get the site message of the day", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SiteStatus.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiStatusResponse.class))),
    })
    @GetMapping(value = {"/status"}, produces = {"application/json"})
    public ResponseEntity<SiteStatus> getSiteStatus() {
        log.debug("API::Retrieving Site Status");
        SiteStatus siteStatus = new SiteStatus("GREEN", "The site is currently healthy");
        return ResponseEntity.ok().body(siteStatus);
    }

}


