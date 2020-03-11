boolean dead = false;
boolean score = false;
int timeSurvived = 0;
float hue = 0;
float maxHue = 5000;
int pulseTimer = 20;
boolean immune = false;
float immunityStart = 0;
float aura = 100;

class Bullet {
    int x, y;
    float h, dx, dy;
    float tx, ty;
    Bullet(int a, int b, float c, float d) {
    x = a;
    y = b;
    dx = c;
    dy = d;
    h = hue;
    hue ++;
    if (hue > maxHue) {
    hue = 0;
}
}
void render() {
    if (!score) {
        colorMode(RGB, 255);
        stroke(255);
        colorMode(HSB, maxHue);
        fill(h, maxHue, maxHue);
        ellipse(x, y, 10, 10);
        colorMode(RGB, 255);
        stroke(0);
    }
}
void move() {
    if(tx != 0 && ty != 0) {
        x += tx - x / 100
        y += ty - y / 100
    } else {
        x += dx;
        y += dy;
        /*x += dx - x / 100
        y += dy - y / 100*/

    }
}
}

ArrayList<Bullet> bullets = new ArrayList<Bullet>();

class Enemy {
    int x, y, shots, timer;
    float theta, pTheta, despawnTimer, spawnTimer;
    char type;
    boolean firing = false;
    void spawn() {
        if (spawnTimer < 255) {
            spawnTimer ++;
        }
    }
    void despawn() {
        despawnTimer ++;
    }
    void render() {
        if (!score) {
            colorMode(RGB, 255);
            strokeWeight(1);
            fill(255, min(pow(spawnTimer - despawnTimer, 2), 255));
            stroke(0, min(pow(spawnTimer - despawnTimer, 2), 255));
            ellipse(x, y, 20, 20);
            fill(0, 102, 153);
            text(type, x - 3, y + 3);
        }
    }
    void renderA() {
        if (!score) {
            strokeWeight(1);
            colorMode(HSB, maxHue);
            fill(hue, maxHue, maxHue, maxHue * min(pow(spawnTimer - despawnTimer, 2), 255) / (255 * 4));
            stroke(hue, maxHue, maxHue, maxHue * min(pow(spawnTimer - despawnTimer, 2), 255) / 255);
            strokeWeight(1);
            ellipse(x, y, 2 * aura * (min(pow((float) spawnTimer - despawnTimer, 2), 255)) / 255, 2 * aura * (min(pow((float) spawnTimer - despawnTimer, 2), 255)) / 255);
            colorMode(RGB, 255);
            strokeWeight(1);
        }
    }
}

ArrayList<Enemy> enemies = new ArrayList<Enemy>();

class Spinner extends Enemy {
    Spinner(int a, int b) {
    x = a;
    y = b;
    type = "s";
}
float theta = random(2 * PI);
void fire() {
    if (firing == false) {
        timer += 1;
        if (timer > 30) {
            if (random(10) < 1) {
                firing = true;
            }
            timer = 0;
        }
    }
    if (firing == true && timer > 180) {
        firing = false;
        timer = 0;
    }
    if (firing)
        timer ++;
    if (firing && timer % 8 == 0 && dist(x, y, mouseX, mouseY) > aura) {
        bullets.add(new Bullet(x, y, cos(theta), sin(theta)));
        bullets.add(new Bullet(x, y, cos(theta + PI / 2), sin(theta + PI / 2)));
        bullets.add(new Bullet(x, y, cos(theta + PI), sin(theta + PI)));
        bullets.add(new Bullet(x, y, cos(theta - PI / 2), sin(theta - PI / 2)));
        theta += 0.2;
        if (theta > 2 * PI) {
            theta -= 2 * PI;
        }
    }
    if(firing && timer % 8 == 0)
        shots += 4;
}
}
class Closer extends Enemy {
    Closer(int a, int b) {
    x = a;
    y = b;
    type = "c";
}
void fire() {
    if (firing == false) {
        timer += 1;
        if (dist(x, y, mouseX, mouseY) > aura && timer == 60 || timer == 70 || timer == 75 || timer == 80 || timer == 85 || timer > 90) {
            noFill();
            stroke(0);
            strokeWeight(5);
            ellipse(mouseX, mouseY, 120, 120);
        }
        if (timer > 100) {
            //if(random(10) < 1) {
            firing = true;
            //}
            timer = 0;
        }
    }
    if (firing == true && timer > 180) {
        firing = false;
        timer = 0;
    }
    if (firing)
        timer ++;
    if (firing && timer == 5 && dist(x, y, mouseX, mouseY) > aura) {
        bullets.add(new Bullet(mouseX + 100, mouseY + 100, -1, -1));
        bullets.add(new Bullet(mouseX - 100, mouseY + 100, 1, -1));
        bullets.add(new Bullet(mouseX + 100, mouseY - 100, -1, 1));
        bullets.add(new Bullet(mouseX - 100, mouseY - 100, 1, 1));
        bullets.add(new Bullet(mouseX + 120, mouseY, -1, 0));
        bullets.add(new Bullet(mouseX - 120, mouseY, 1, 0));
        bullets.add(new Bullet(mouseX, mouseY + 120, 0, -1));
        bullets.add(new Bullet(mouseX, mouseY - 120, 0, 1));

    }
    if(firing && timer % 8 == 0)
        shots += 1;
}
}

class Single extends Enemy {
    int aimSpeed = 0.03;
    Single(int a, int b) {
    x = a;
    y = b;
    type = "a";
    if (x < mouseX) {
    theta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
} else {
    theta = PI + asin((y - mouseY) / dist(x, y, mouseX, mouseY));
}
}
void fire() {
    if (theta < 0) {
        theta += 2 * PI;
    }
    theta %= 2 * PI;
    if (x < mouseX) {
        //reset angle to [0, 2pi]
        if (-asin((y - mouseY) / dist(x, y, mouseX, mouseY)) < 0) {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY)) + 2 * PI;
        } else {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
        }
        //tracking code
        if (y > mouseY) {
            if (theta > pTheta) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else if (pTheta - theta < PI) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            }
        } else {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
            if (theta < pTheta) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else if (theta - pTheta < PI) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            }
        }
    } else {
        pTheta = PI + asin((y - mouseY) / dist(x, y, mouseX, mouseY));
        if (y < mouseY) {
            if (theta < pTheta) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else if (theta - pTheta < PI) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            }
        } else {
            if(theta > pTheta){
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else if (pTheta - theta < PI) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            }
        }
    }
    if (firing == false) {
        timer += 1;
        if (timer > 5) {
            if (random(10) < 1) {
                firing = true;
            }
            timer = 0;
        }
    }
    if (firing == true && timer > 16) {
        firing = false;
        timer = 0;
    }
    if (firing) {
        if (timer % 3 == 0 && dist(x, y, mouseX, mouseY) > aura) {
            bullets.add(new Bullet(x, y, cos(theta) * 3, sin(theta) * 3));
        }
        if(timer % 3 == 0)
            shots ++;
        timer ++;
    }
}
}

class Spread extends Enemy {
    int aimSpeed = 0.03;
    Spread(int a, int b) {
    x = a;
    y = b;
    type = "w";
    if (x < mouseX) {
    theta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
} else {
    theta = PI + asin((y - mouseY) / dist(x, y, mouseX, mouseY));
}
}
void fire() {
    if (theta < 0) {
        theta += 2 * PI;
    }
    theta %= 2 * PI;
    if (x < mouseX) {
        //reset angle to [0, 2pi]
        if (-asin((y - mouseY) / dist(x, y, mouseX, mouseY)) < 0) {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY)) + 2 * PI;
        } else {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
        }
        //tracking code
        if (y > mouseY) {
            if (theta > pTheta) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else if (pTheta - theta < PI) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            }
        } else {
            pTheta = -asin((y - mouseY) / dist(x, y, mouseX, mouseY));
            if (theta < pTheta) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else if (theta - pTheta < PI) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            }
        }
    } else {
        pTheta = PI + asin((y - mouseY) / dist(x, y, mouseX, mouseY));
        if (y < mouseY) {
            if (theta < pTheta) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else if (theta - pTheta < PI) {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            }
        } else {
            if(theta > pTheta){
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            } else if (pTheta - theta < PI) {
                if (theta + aimSpeed > pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta += aimSpeed;
                }
            } else {
                if (theta - aimSpeed < pTheta && abs(pTheta - theta) < PI) {
                    theta = pTheta;
                } else {
                    theta -= aimSpeed;
                }
            }
        }
    }
    if (firing == false) {
        timer += 1;
        if (timer > 5) {
            if (random(10) < 1) {
                firing = true;
            }
            timer = 0;
        }
    }
    if (firing == true && timer > 16) {
        firing = false;
        timer = 0;
    }
    if (firing) {
        if (timer % 12 == 0 && dist(x, y, mouseX, mouseY) > aura) {
            bullets.add(new Bullet(x, y, cos(theta - PI / 6) * 3, sin(theta - PI / 6) * 3));
            bullets.add(new Bullet(x, y, cos(theta - PI / 12) * 3, sin(theta - PI / 12) * 3));
            bullets.add(new Bullet(x, y, cos(theta + PI / 12) * 3, sin(theta + PI / 12) * 3));
            bullets.add(new Bullet(x, y, cos(theta + PI / 6) * 3, sin(theta + PI / 6) * 3));
        }
        if(timer % 12 == 0){
            shots += 4;
        }
        timer ++;
    }
}
}

void die(Bullet b) {
    if (dist(mouseX, mouseY, b.x, b.y) < 8) {
        if (immune){
            bullets.remove(b);
            immune = false;
        } else {
            fill(0);
            textSize(50);
            if (!score) {
                text("You Died.", random(width), random(height));
            }
            dead = true;
        }
    }
}

int enemiesAmt = 0;

void mouseClicked() {
    if(mouseButton == LEFT){
        if(immunityStart <= 0)
            immune = true;
    } else {
        if (!dead) {
            enemiesAmt ++;
            spawnEnemy();
        } else {
            score = true;
            background(255);
            textSize(20);
            text("Score: " + timeSurvived + " frames.", 300, 300);
        }
    }
}

void deco() {
    strokeWeight(20);
    colorMode(HSB, maxHue);
    stroke(hue, maxHue, maxHue);
    line(0, 0, width, 0);
    line(width, 0, width, height);
    line(width, height, 0, height);
    line(0, height, 0, 0);
    strokeWeight(1);
    colorMode(RGB, 255);
}

class Pulse {
    int x, y, radius;
    Pulse(int a, int b) {
    x = a;
    y = b;
    radius = 0;
}
void render() {
    if (!score) {
        noFill();
        colorMode(HSB, maxHue);
        stroke(hue, maxHue, maxHue, 100 * maxHue / radius);
        strokeWeight(1);
        ellipse(x, y, radius, radius);
        colorMode(RGB, 255);
        radius ++;
    }
}
}

ArrayList<Pulse> pulses = new ArrayList<Pulse>();

void immunityAnimation() {
    colorMode(HSB, maxHue);
    stroke(hue, maxHue, maxHue);
    if (immunityStart < 1 && immune) {
        immunityStart += 0.05;
    }
    if (immunityStart > 0 && !immune) {
        immunityStart -= 0.05;
    }
    noFill();
    strokeWeight(3);
    ellipse(mouseX, mouseY, 30 * (pow(immunityStart - 1, 3) + 1), 30 * (pow(immunityStart - 1, 3) + 1));
    colorMode(RGB, 255);
}
void spawnEnemy(){
    //enemies.add(new Closer((int) random(width), (int) random(height)));
    switch((int) random(4)){
    case 0:
        enemies.add(new Spinner((int) random(width), (int) random(height)));
        break;
    case 1:
        enemies.add(new Single((int) random(width), (int) random(height)));
        break;
    case 2:
        enemies.add(new Spread((int) random(width), (int) random(height)));
        break;
    case 3:
        enemies.add(new Closer((int) random(width), (int) random(height)));
        break;
    }
}

void setup() {
    size(1000, 600, P2D);
    noCursor();
}

void draw() {
    if (!dead) {
        background(255);
        colorMode(HSB, maxHue);
        fill(hue, maxHue, maxHue);
        ellipse(mouseX, mouseY, 5, 5);
        colorMode(RGB, 255);
        immunityAnimation();
        timeSurvived ++;
    }

    if (timeSurvived % 20 == 0) {
        pulses.add(new Pulse(mouseX, mouseY));
    }

    for (int i = 0; i < pulses.size(); i ++) {
        Pulse p = pulses.get(i);
        p.render();
        if (p.radius > (int) dist(0, 0, width, height)) {
            pulses.remove(p);
            i --;
        }
    }

    if (enemies.size() < enemiesAmt) {
        spawnEnemy();
    }

    for (int i = 0; i < enemies.size(); i ++) {
        Enemy e = enemies.get(i);
        e.spawn();
        e.renderA();
        e.fire();
        if (e.shots > 100){
            e.despawn();
            if (e.despawnTimer >= 255) {
                enemies.remove(e);
                i --;
            }
        }
    }

    for(Enemy e : enemies){
        e.render();

    }

    for (int i = 0; i < bullets.size(); i ++) {
        Bullet b = bullets.get(i);
        b.render();
        b.move();
        if (b.x > width || b.x < 0 || b.y > height || b.y < 0) {
            bullets.remove(b);
            i --;
        }
        die(b);
    }
    deco();
}
