# Tamanoir API

## Create a new connection

Return new connection.

Method - POST

URL "**/connections**"

Argument | Type/Value | Description 
:--- | :--- | :---
connectionDescriptor | QueryConnectionDescriptor required | **Example:** {"url":"jdbc:postgresql://localhost:5432/foodmart","type":"jdbc","properties":{"user":"postgres","password":"postgres"}}
accept | Header required | Option, which determines adding metadata. **Example:** accept “application/metadata+json” – metadata will be added to responce
include | String | Will include metadata for specified in response. Can be used for multiple nodes. Mutual exclusive with “expand”. **Example:** “/rest/connections?include=public”
expand | String | Will add all children nodes to response. Mutual exclusive with “include”. **Example:** “/rest/connections?expand=public”
recursive | String | Will include metadata for specified node with metadata  for all children nodes. Can be used for multiple nodes. **Example:** “/rest/connections?recursive=public”

Options
* consumes: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Connection created. | 404 Not Found –


## Execute query

Execute query that come in connectionDescriptor.

Method - POST

URL "**/connections**"

Argument | Type/Value | Description 
:--- | :--- | :---
connectionDescriptor | QueryConnectionDescriptor required | **Example:** {"url":"jdbc:postgresql://localhost:5432/foodmart","type":"jdbc","properties":{"user":"postgres","password":"postgres"}}

Options
* consumes: application/queryconnection+json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Query executed. | 404 Not Found –


## Get DataSet Metadata

Return metadata for dataset specified by UUID.

Method - GET

URL "**/connections/{uuid}/metadata**"

Argument | Type/Value | Description 
:--- | :--- | :---
uuid | UUID required | Specify dataset

Options
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Dataset metadata found and returned in responce. | 404 Not Found –


## Get Connection descriptor

Return connection descriptor by UUID.

Method - GET

URL "**/connections/{uuid}**"

Argument | Type/Value | Description 
:--- | :--- | :---
uuid | UUID required | Specify dataset

Options
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Connection descriptor found. | 404 Not Found –


## Execute query

Execute query by UUID.

Method - POST

URL "**/connections/{uuid}**"

Argument | Type/Value | Description 
:--- | :--- | :---
uuid | UUID required | Specify dataset
query | UnifiedTableQuery required | Query to execute

Options
* consumes: application/json (default)
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Query executed. | 404 Not Found –


## Create domain

Create domain.

Method - POST

URL "**/domains**"

Argument | Type/Value | Description 
:--- | :--- | :---
domain | Domain required | Specify domain

Options
* consumes: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Domain created. | 404 Not Found –


## Read domain

Read domain.

Method - GET

URL "**/domains/{id}**"

Argument | Type/Value | Description 
:--- | :--- | :---
id | Long required | Specify domain

Options
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Domain found. | 404 Not Found –


## Read all domains

Read all domains.

Method - GET

URL "**/domains**"

Argument | Type/Value | Description 
:--- | :--- | :---

Options
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Domain found. | 404 Not Found –


## Update domain

Updates domain.

Method - PUT

URL "**/domains/{id}**"

Argument | Type/Value | Description 
:--- | :--- | :---
id | Long required | Specify domain to update
domain | Domain required | Specify updates to domain

Options
* consumes: application/json (default)
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Domain updated. | 404 Not Found –


## Delete domain

Deletes domain.

Method - DELETE

URL "**/domains/{id}**"

Argument | Type/Value | Description 
:--- | :--- | :---
id | Long required | Specify domain

Options
* produces: application/json (default)

Return Value on Success | Typical Return Values on Failure
--- | ---
200 OK – Domain updated. | 404 Not Found –