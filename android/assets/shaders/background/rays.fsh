// Author:
// Title:

#ifdef GL_ES
precision lowp float;
#endif

#define PI 3.1415926535897932384626433832795

uniform vec2 u_resolution;
uniform float u_time;

// bool should(float n) {
//     for (float a = 0.0; a <= PI*2.0; a+=PI/16.000) {
//         if (n > tan(a) && n < tan(a+PI/50.0)) {
//             return true;
//         }
//     }
// }

float d = PI/6.0;

//PI/13.0
//PI/50.0

float speed = 2.0;
float to = 3.0;
float plot(float a) {
    for (float i = 0.0 + u_time*speed; i <= to + u_time*speed; i+=1.0) {
        float start = i*PI/to;
//        float tanMin = tan(start - d);
//        float tanCenter = tan(start);
//        float tanMax = tan(start + d);
        float tanMin = tan(start) - d;
        float tanCenter = tan(start);
        float tanMax = tan(start) + d;
        if (a > tanMin && a < tanMax) {
            return smoothstep(tanMin, tanCenter, a)
            - smoothstep(tanCenter, tanMax, a);
        }
    }
//    float a = sin(u_time) * PI;
//    return smoothstep(tan(a-d), tan(a), n)
//    - smoothstep(tan(a), tan(a+d), n);

    return 0.0;
}



void main() {
    vec2 pos = gl_FragCoord.xy;
//    float x = pos.x; float y = pos.y;
//    pos.x *= sin(u_time);
//    pos.y *= cos(u_time);

//    pos.x


    vec2 st = (pos.xy / u_resolution - 0.5);

    vec4 color;

    // if (should(st.y / st.x)) {
    // 	color = vec4(1.0, 1.0, 1.0, 1.0);
    // } else {
    // 	color = vec4(0.0, 0.0, 1.0, 1.0);
    // }

    // if (st.y / st.x > tan(PI*2.0)) {
    // 	color = vec4(1.0, 1.0, 1.0, 1.0);
    // } else {
    // 	color = vec4(0.0, 0.0, 1.0, 1.0);
    // }
    float a = st.y/st.x;
//    if (st.x < d) {
//        st.x = d;
//    }

//    if (a > tan(PI/3.0) || a < tan(2.0*PI/3.0)) a = tan(PI/3.0);



    color = vec4(1.0, 1.0, 1.0, plot(a)*length(st));


    gl_FragColor = color;
}