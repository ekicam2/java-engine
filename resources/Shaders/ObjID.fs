#version 330 core

uniform vec3 object_id;

out vec3 color;

void main()
{
    color = object_id;
}