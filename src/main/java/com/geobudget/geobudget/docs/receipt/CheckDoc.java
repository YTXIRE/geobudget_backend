package com.geobudget.geobudget.docs.receipt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Получение чека", description = "Получение чека из ФНС")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                        value = "Bad request"
                ))
        )
})
@RequestBody(
        required = true,
        content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                        value = "{\"qr\": \"t=20201017T1923&s=1498.00&fn=9282440300669857&i=25151&fp=1186123459&n=1\"}"
                )
        )
)
public @interface CheckDoc {}
