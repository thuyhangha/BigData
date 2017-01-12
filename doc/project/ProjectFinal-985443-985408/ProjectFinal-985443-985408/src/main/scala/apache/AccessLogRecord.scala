package apache

case class AccessLogRecord (
    clientIpAddress: String,         // should be an ip address, but may also be the hostname if hostname-lookups are enabled
    rfc1413ClientIdentity: String,   // typically `-`
    remoteUser: String,              // typically `-`
    dateTime: String,                // [day/month/year:hour:minute:second zone]
    request: String,                 // `GET /foo ...`
    httpStatusCode: String,          // 200, 404, etc.
    bytesSent: String               // may be `-`
)
