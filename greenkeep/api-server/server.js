var ApiServer = require('apiserver')
var Eos = require('eosjs')


Eos = require('eosjs')


// Default configuration (additional options below)
config = {
  chainId: null, // 32 byte (64 char) hex string
  keyProvider: ['5K3KkzcdeSYaL27GM6WpT16qRTvBjPjANdjqhdbFre1bktohrxF'], // WIF string or array of keys..
  httpEndpoint: 'http://192.168.137.149:8888',
  expireInSeconds: 60,
  broadcast: true,
  verbose: false, // API activity
  sign: true,
  authorization: 'scanner@active'
   
}

eos = Eos(config)


var apiServer = new ApiServer({ port: 8080 })

// middleware
apiServer.use(/^\/admin\//, ApiServer.httpAuth({
  realm: 'ApiServer Example',
  encode: true,
  credentials: ['admin:apiserver']
}))
apiServer.use(ApiServer.payloadParser())

// modules
apiServer.addModule('1', 'fooModule', {
  // only functions exposed
    
  foo: {
    get: async function (request, response) {
      response.serveJS
        
    response.serveJSON({
        id: request.querystring.id,
        verbose: request.querystring.verbose,
        result: await eos.getTableRows({
      "json": true,
      "code": "scanner",   // contract who owns the table
      "scope": "scanner",  // scope of the table
      "table": "sensordata",    // name of the table as specified by the contract abi
      "limit": 100,
    })
      })
    },
    post: function (request, response) {
      request.resume()
    
      request.once('end', function () {
          
        eos.transaction(
          {
            // ...headers,
            actions: [
              {
                account: 'scanner',
                name: 'adddata',
                authorization: [{
                  actor: 'scanner',
                  permission: 'active'
                }],
                data: {
                  sensor: 'scanner',
                  distance_traveled_per_day: request.body.distance_traveled_per_day,
                  weight: request.body.weight,
                  age: request.body.age,
                  batchdate: request.body.batchdate,
                  location: request.body.location
                }
              }
            ]
          }
          // options -- example: {broadcast: false}
        )
          
          
          
        response.serveJSON({
          id: request.querystring.id,
          verbose: request.querystring.verbose,
          method: 'POST',
          payload: request.body // thanks to payloadParser
        })
      })
    }
  },
  bar: function (request, response) {
    response.serveJSON({ foo: 'bar', pow: this._pow(5), method: '*/' + request.method })
  },
  // never exposed due to the initial underscore
  _pow: function (n) {
    return n * n
  }
})

// custom routing
apiServer.router.addRoutes([
  ['/foo', '1/fooModule#foo'],
  ['/foo/:id/:verbose', '1/fooModule#foo'],
  ['/foo_verbose/:id', '1/fooModule#foo', { 'verbose': true }],
  ['/bar', '1/fooModule#bar', {}, true] // will keep default routing too
])

// events
apiServer.on('requestStart', function (pathname, time) {
  console.info(' ☉ :: start    :: %s', pathname)
}).on('requestEnd', function (pathname, time) {
  console.info(' ☺ :: end      :: %s in %dms', pathname, time)
}).on('error', function (pathname, err) {
  console.info(' ☹ :: error    :: %s (%s)', pathname, err.message)
}).on('timeout', function (pathname) {
  console.info(' ☂ :: timedout :: %s', pathname)
})

apiServer.listen()


