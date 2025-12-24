code that maps a texture to a polygon.
first triangulate polygon into triangles using modified ear-clipping algorithm (to avoid sliver triangles)
then render each triangle separately, samplign teh given texture "smile.png" to the overall screen
polygon can be transformed in any way and the triangle rendering should still work
