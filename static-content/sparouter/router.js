const routes = []
let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(pathTemplate, handler){
    routes.push({pathTemplate, handler})
}
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

// TODO: refactor for readability and do tests to ensure every edge case is working
function getRouteHandler(path){
    const queryStart = path.indexOf("?")
    if (queryStart !== -1)
        path = path.slice(0, queryStart)
    const receivedPathSplit = path.split("/")

    const possibleRoutes = routes.filter((route) =>
        route.pathTemplate.split("/").length === receivedPathSplit.length
    )
    if (possibleRoutes.length === 0) return notFoundRouteHandler

    const validRoutes = new Array(possibleRoutes.length).fill(true)

    for (let i = 0; i < receivedPathSplit.length; i++) {
        const receivedPathPart = receivedPathSplit[i]

        for (let j = 0; j < possibleRoutes.length; j++) {
            const storedPathPart = possibleRoutes[j].pathTemplate.split("/")[i]
            if (storedPathPart === "*") continue
            else if (receivedPathPart !== storedPathPart) validRoutes[j] = false
        }
    }

    const validRouteIdx = validRoutes.findIndex((validOrNot) => validOrNot === true)
    if (validRouteIdx === -1) return notFoundRouteHandler
    return possibleRoutes[validRouteIdx].handler
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler
}

export default router