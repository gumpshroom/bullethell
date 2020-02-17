import processing.core.*;
import processing.opengl.PJOGL;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class bullethell extends PApplet {

    /*boolean dead = false;
    boolean score = false;
    double timeSurvived = 0;
    double hue = 0;
    double maxHue = 5000;
    double pulseTimer = 20;
    boolean immune = false;
    double immunityStart = 0;
    double aura = 100;
    */
    //icon
    PImage fai_iconi;
    PGraphics fai_icong;
    String fai_filename;

    private boolean dead;
    private boolean score;
    private double timeSurvived;
    private double hue;
    private double maxHue;
    private double pulseTimer;
    private boolean immune;
    private double immunityStart;
    private double aura;
    private PFont font;
    class Bullet {
        double x, y;
        double h, dx, dy;
        double tx, ty;

        Bullet(double a, double b, double c, double d) {
            x = a;
            y = b;
            dx = c;
            dy = d;
            h = hue;
            hue++;
            if (hue > maxHue) {
                hue = 0;
            }
        }

        void render() {
            if (!score) {
                colorMode(RGB, 255);
                hint(ENABLE_STROKE_PURE);
                stroke(255);
                colorMode(HSB, (float) maxHue);
                fill((float) h, (float) maxHue, (float) maxHue);
                ellipse((float) x, (float) y, 10, 10);
                colorMode(RGB, 255);
                stroke(0);
            }
        }

        void move() {

            x += dx;
            y += dy;
      /*x += dx - x / 100
      y += dy - y / 100*/


        }
    }
    class Beam {
        double x, y;
        double h, dx, dy;
        double tx, ty;
        int frame;
        Beam(double a, double b, double c, double d) {
            x = a;
            y = b;
            dx = c;
            dy = d;
            h = hue;
            hue++;
            frame = 0;
            if (hue > maxHue) {
                hue = 0;
            }
        }

        void render() {
            if (!score) {
                colorMode(RGB, 255);
                hint(ENABLE_STROKE_PURE);
                colorMode(HSB, (float) maxHue);
                //fill((float) h, (float) maxHue, (float) maxHue);
                stroke((float) h, (float) maxHue, (float) maxHue);
                int framecount = (frame % 2) * 5 + 8;
                strokeWeight(framecount);
                line((float) x, (float) y, (float) (dx), (float) (dy));
                colorMode(RGB, 255);
                stroke(0);
                strokeWeight(1);
                frame++;
            }
        }

    }
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Beam> beams = new ArrayList<>();

    abstract class Enemy {
        double x, y, shots, timer;
        double theta, pTheta, despawnTimer, spawnTimer;
        String type;
        boolean firing = false;

        void spawn() {
            if (spawnTimer < 255) {
                spawnTimer++;
            }
        }

        void despawn() {
            despawnTimer++;
        }

        abstract public void fire();

        void render() {
            if (!score) {
                colorMode(RGB, 255);
                hint(ENABLE_STROKE_PURE);
                strokeWeight(1);
                fill(255, min(pow((float) spawnTimer - (float) despawnTimer, 2), 255));
                stroke(0, min(pow((float) spawnTimer - (float) despawnTimer, 2), 255));
                ellipse((float) x, (float) y, 20, 20);
                fill(0, 102, 153);

                textFont(font);
                textSize(12);
                text(type, (float) x - 3, (float) y + 3);
            }
        }

        void renderA() {
            if (!score) {
                strokeWeight(1);
                colorMode(HSB, (float) maxHue);
                hint(ENABLE_STROKE_PURE);
                fill((float) hue, (float) maxHue, (float) maxHue, (float) maxHue * min(pow((float) spawnTimer - (float) despawnTimer, 2), 255) / (255 * 4));
                stroke((float) hue, (float) maxHue, (float) maxHue, (float) maxHue * min(pow((float) spawnTimer - (float) despawnTimer, 2), 255) / 255);
                strokeWeight(1);
                ellipse((float) x, (float) y, (float) (2 * aura * (min(pow((float) spawnTimer - (float) despawnTimer, 2), 255)) / 255), (float) (2 * aura * (min(pow((float) spawnTimer - (float) despawnTimer, 2), 255)) / 255));
                colorMode(RGB, 255);
                strokeWeight(1);
            }
        }
    }

    private ArrayList<Enemy> enemies = new ArrayList<>();

    class Spinner extends Enemy {
        Spinner(double a, double b) {
            x = a;
            y = b;
            type = "s";
        }

        double theta = random(TWO_PI);

        public void fire() {
            if (!firing) {
                timer += 1;
                if (timer > 30) {
                    if (random(10) < 1) {
                        firing = true;
                    }
                    timer = 0;
                }
            }
            if (firing && timer > 180) {
                firing = false;
                timer = 0;
            }
            if (firing)
                timer++;
            if (firing && timer % 8 == 0 && distance(x, y, mouseX, mouseY) > aura) {
                bullets.add(new Bullet(x, y, Math.cos(theta), Math.sin(theta)));
                bullets.add(new Bullet(x, y, Math.cos(theta + PI / 2), Math.sin(theta + PI / 2)));
                bullets.add(new Bullet(x, y, Math.cos(theta + PI), Math.sin(theta + PI)));
                bullets.add(new Bullet(x, y, Math.cos(theta - PI / 2), Math.sin(theta - PI / 2)));
                theta += 0.2f;
                if (theta > TWO_PI) {
                    theta -= TWO_PI;
                }
            }
            if (firing && timer % 8 == 0)
                shots += 4;
        }
    }

    class Closer extends Enemy {
        Closer(double a, double b) {
            x = a;
            y = b;
            type = "c";
        }

        public void fire() {
            if (!firing) {
                timer += 1;
                if (distance(x, y, mouseX, mouseY) > aura && timer == 60 || timer == 70 || timer == 75 || timer == 80 || timer == 85 || timer > 90) {
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
            if (firing && timer > 180) {
                firing = false;
                timer = 0;
            }
            if (firing)
                timer++;
            if (firing && timer == 5 && distance(x, y, mouseX, mouseY) > aura) {
                bullets.add(new Bullet(mouseX + 100, mouseY + 100, -1, -1));
                bullets.add(new Bullet(mouseX - 100, mouseY + 100, 1, -1));
                bullets.add(new Bullet(mouseX + 100, mouseY - 100, -1, 1));
                bullets.add(new Bullet(mouseX - 100, mouseY - 100, 1, 1));
                bullets.add(new Bullet(mouseX + 120, mouseY, -1, 0));
                bullets.add(new Bullet(mouseX - 120, mouseY, 1, 0));
                bullets.add(new Bullet(mouseX, mouseY + 120, 0, -1));
                bullets.add(new Bullet(mouseX, mouseY - 120, 0, 1));

            }
            if (firing && timer % 8 == 0)
                shots += 1;
        }
    }

    class Single extends Enemy {
        double aimSpeed = 0.03f;

        Single(double a, double b) {
            x = a;
            y = b;
            type = "a";
            if (x < mouseX) {
                theta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
            } else {
                theta = PI + Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
            }
        }

        public void fire() {
            if (theta < 0) {
                theta += TWO_PI;
            }
            theta %= TWO_PI;
            if (x < mouseX) {
                //reset angle to [0, 2pi]
                if (-Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY)) < 0) {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY)) + TWO_PI;
                } else {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                }
                //tracking code
                if (y > mouseY) {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                } else {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                }
            } else {
                pTheta = PI + Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                if (y < mouseY) {
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                } else {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                }
            }
            if (!firing) {
                timer += 1;
                if (timer > 5) {
                    if (random(10) < 1) {
                        firing = true;
                    }
                    timer = 0;
                }
            }
            if (firing && timer > 16) {
                firing = false;
                timer = 0;
            }
            if (firing) {
                if (timer % 3 == 0 && distance(x, y, mouseX, mouseY) > aura) {
                    bullets.add(new Bullet(x, y, Math.cos(theta) * 3, Math.sin(theta) * 3));
                }
                if (timer % 3 == 0)
                    shots++;
                timer++;
            }
        }
    }

    class Spread extends Enemy {
        double aimSpeed = 0.03f;

        Spread(double a, double b) {
            x = a;
            y = b;
            type = "w";
            if (x < mouseX) {
                theta = -Math.asin((y - mouseY) / distance((float) x, (float) y, mouseX, mouseY));
            } else {
                theta = PI + Math.asin((y - mouseY) / distance((float) x, (float) y, mouseX, mouseY));
            }
        }

        @Override
        public void fire() {
            if (theta < 0) {
                theta += TWO_PI;
            }
            theta %= TWO_PI;
            if (x < mouseX) {
                //reset angle to [0, 2pi]
                if (-Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY)) < 0) {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY)) + TWO_PI;
                } else {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                }
                //tracking code
                if (y > mouseY) {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                } else {
                    pTheta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                }
            } else {
                pTheta = PI + Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
                if (y < mouseY) {
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                } else {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                }
            }
            if (!firing) {
                timer += 1;
                if (timer > 5) {
                    if (random(10) < 1) {
                        firing = true;
                    }
                    timer = 0;
                }
            }
            if (firing&& timer > 16) {
                firing = false;
                timer = 0;
            }
            if (firing) {
                if (timer % 12 == 0 && distance(x, y, mouseX, mouseY) > aura) {
                    bullets.add(new Bullet(x, y, Math.cos(theta - PI / 6) * 3, Math.sin(theta - PI / 6) * 3));
                    bullets.add(new Bullet(x, y, Math.cos(theta - PI / 12) * 3, Math.sin(theta - PI / 12) * 3));
                    bullets.add(new Bullet(x, y, Math.cos(theta + PI / 12) * 3, Math.sin(theta + PI / 12) * 3));
                    bullets.add(new Bullet(x, y, Math.cos(theta + PI / 6) * 3, Math.sin(theta + PI / 6) * 3));
                }
                if (timer % 12 == 0) {
                    shots += 4;
                }
                timer++;
            }
        }
    }
    class Beamer extends Enemy {
        double aimSpeed = 0.03f;
        double mx, my;
        Beamer(double a, double b) {
            x = a;
            y = b;
            type = "b";
            if (x < mouseX) {
                theta = -Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
            } else {
                theta = PI + Math.asin((y - mouseY) / distance(x, y, mouseX, mouseY));
            }
        }

        public void fire() {
            if (theta < 0) {
                theta += TWO_PI;
            }
            theta %= TWO_PI;
            if (x < mx) {
                //reset angle to [0, 2pi]
                if (-Math.asin((y - my) / distance(x, y, mx, my)) < 0) {
                    pTheta = -Math.asin((y - my) / distance(x, y, mx, my)) + TWO_PI;
                } else {
                    pTheta = -Math.asin((y - my) / distance(x, y, mx, my));
                }
                //tracking code
                if (y > my) {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                } else {
                    pTheta = -Math.asin((y - my) / distance(x, y, mx, my));
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                }
            } else {
                pTheta = PI + Math.asin((y - my) / distance(x, y, mx, my));
                if (y < my) {
                    if (theta < pTheta) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else if (theta - pTheta < PI) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    }
                } else {
                    if (theta > pTheta) {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    } else if (pTheta - theta < PI) {
                        if (theta + aimSpeed > pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta += aimSpeed;
                        }
                    } else {
                        if (theta - aimSpeed < pTheta && Math.abs(pTheta - theta) < PI) {
                            theta = pTheta;
                        } else {
                            theta -= aimSpeed;
                        }
                    }
                }
            }
            if (!firing) {
                timer += 1;
                if (timer == 50) {
                    mx = mouseX;
                    my = mouseY;
                }
                if (distance(x, y, mouseX, mouseY) > aura && timer == 50 || timer == 60 || timer == 70 || timer == 75 || timer == 80 || timer == 85 || timer > 90) {
                    noFill();
                    colorMode(HSB, (float) maxHue);
                    hint(ENABLE_STROKE_PURE);
                    stroke((float)(hue + maxHue / 2 % maxHue), (float) maxHue, (float) (maxHue / 1.5));
                    strokeWeight(5);
                    ellipse((float)mx, (float)my, 60, 60);
                    colorMode(RGB);
                }
                if (timer > 100) {
                    //if(random(10) < 1) {
                    firing = true;
                    //}
                    timer = 0;
                }
            }
            if (firing && timer > 180) {
                firing = false;
                timer = 0;
            }
            if (firing) {
                double dx = Math.cos(theta) * 3;
                double dy = Math.sin(theta) * 3;

                if (timer == 5 && distance(x, y, mouseX, mouseY) > aura) {
                    //System.out.println((toDegrees(pTheta) + 90) % 360);
                    //System.out.println(Math.cos(pTheta) * distance(x, y, mouseX, mouseY) * 10 + x);
                    //System.out.println(mouseX);
                    beams.add(new Beam(x, y, Math.cos(pTheta) * distance(x, y, mx, my) * 10 + x, Math.sin(pTheta) * distance(x, y, mx, my) * 10 + y));
                }
                if (timer % 8 == 0)
                    shots++;
                timer++;
            }
        }
    }
    private void bulletdie(Bullet b) {
        if (distance(mouseX, mouseY, b.x, b.y) < 8) {
            if (immune) {
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
    private void beamdie(Beam b) {
        int framecount = 10;
        //int framecount = (b.frame % 2) * 5 + 8;
        double[] vertx = {b.x - framecount, b.x + framecount, b.dx - framecount, b.dx + framecount};
        double[] verty = {b.y - framecount, b.y + framecount, b.dy - framecount, b.dy + framecount};
        if (pnpoly(vertx, verty, mouseX, mouseY)) {
            fill(0);
            textSize(50);
            if (!score) {
                text("You Died.", random(width), random(height));
            }
            dead = true;

        }
    }
    private double enemiesAmt = 0;

    public void mouseClicked() {
        if (mouseButton == LEFT) {
            if(!dead) {
                if (immunityStart <= 0) {
                    immune = true;
                }
            } else if (score) {
                enemies.clear();
                bullets.clear();
                enemiesAmt = 0;
                score = false;
                dead = false;
                pulses.clear();
                immune = false;
                reset();
            }
        } else {
            if (!dead) {
                enemiesAmt++;
                spawnEnemy();
            } else if (!score) {
                score = true;
                background(255);
                textSize(20);
                text("Score: " + timeSurvived + " frames.", 300, 300);
            }
        }
    }

    private void deco() {
        strokeWeight(20);
        colorMode(HSB, (float) maxHue);
        hint(ENABLE_STROKE_PURE);
        stroke((float) hue, (float) maxHue, (float) maxHue);
        line(0, 0, width, 0);
        line(width, 0, width, height);
        line(width, height, 0, height);
        line(0, height, 0, 0);
        strokeWeight(1);
        colorMode(RGB, 255);
    }

    class Pulse {
        double x, y, radius;

        Pulse(double a, double b) {
            x = a;
            y = b;
            radius = 0;
        }

        void render() {
            if (!score) {
                noFill();
                colorMode(HSB, (float) maxHue);
                hint(ENABLE_STROKE_PURE);
                stroke((float) hue, (float) maxHue, (float) maxHue, (float) (100 * maxHue / radius));
                strokeWeight(1);
                ellipse((float) x, (float) y, (float) radius, (float) radius);
                colorMode(RGB, 255);
                radius++;
            }
        }
    }

    private ArrayList<Pulse> pulses = new ArrayList<>();

    private void immunityAnimation() {
        colorMode(HSB, (float) maxHue);
        hint(ENABLE_STROKE_PURE);
        stroke((float) hue, (float) maxHue, (float) maxHue);
        if (immunityStart < 1 && immune) {
            immunityStart += 0.05f;
        }
        if (immunityStart > 0 && !immune) {
            immunityStart -= 0.05f;
        }
        noFill();
        strokeWeight(3);
        ellipse(mouseX, mouseY, 30 * (pow((float) immunityStart - 1, 3) + 1), 30 * (pow((float) immunityStart - 1, 3) + 1));
        colorMode(RGB, 255);
    }

    private void spawnEnemy() {
        //enemies.add(new Beamer(random(width), random(height)));
        switch ((int) random(5)) {
            case 0:
                enemies.add(new Spinner(random(width), random(height)));
                break;
            case 1:
                enemies.add(new Single(random(width), random(height)));
                break;
            case 2:
                enemies.add(new Spread(random(width), random(height)));
                break;
            case 3:
                enemies.add(new Closer(random(width), random(height)));
                break;
            case 4:
                enemies.add(new Beamer(random(width), random(height)));
                break;
        }
    }
    private void reset() {
        dead = false;
        score = false;
        timeSurvived = 0;
        hue = 0;
        maxHue = 5000;
        pulseTimer = 20;
        immune = false;
        immunityStart = 0;
        aura = 100;
    }
    public void setup() {
        font = createFont("Arial", 20);
        noCursor();
        /*String[] lines = loadStrings("trash.txt");
        println("there are " + lines.length + " lines");
        for (int i = 0 ; i < lines.length; i++) {
            println(lines[i]);
        }
*/
        //PImage icon = loadImage("iconbh1.png");
        //surface.setIcon(icon);
        changeAppTitle("nut");
        //hint(ENABLE_STROKE_PURE);
        reset();
    }

    public void draw() {
        if (!dead) {
            background(255);
            colorMode(HSB, (float) maxHue);
            hint(ENABLE_STROKE_PURE);
            fill((float) hue, (float) maxHue, (float) maxHue);
            ellipse(mouseX, mouseY, 5, 5);
            colorMode(RGB, 255);
            immunityAnimation();
            timeSurvived++;
        }

        if (timeSurvived % 20 == 0) {
            pulses.add(new Pulse(mouseX, mouseY));
        }

        for (int i = 0; i < pulses.size(); i++) {
            Pulse p = pulses.get(i);
            p.render();
            if (p.radius > distance(0, 0, width, height)) {
                pulses.remove(p);
                i--;
            }
        }

        if (enemies.size() < enemiesAmt) {
            spawnEnemy();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.spawn();
            e.renderA();
            e.fire();
            if (e.shots > 100) {
                e.despawn();
                if (e.despawnTimer >= 255) {
                    enemies.remove(e);
                    i--;
                }
            }
        }

        for (Enemy e : enemies) {
            e.render();

        }
        for (int j  = 0; j < beams.size(); j++) {
            Beam b = beams.get(j);
            b.render();
            beamdie(b);
            if (b.frame >= 20) {
                beams.remove(b);
                j--;
            }

        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.render();
            b.move();
            if (b.x > width || b.x < 0 || b.y > height || b.y < 0) {
                bullets.remove(b);
                i--;
            }
            bulletdie(b);
        }
        deco();
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

    //processing config
    public void settings() {
        size(1000, 600, P2D);
        PJOGL.setIcon("iconbh1.png");
    }

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"bullethell"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
    static boolean pnpoly(double[] vertx, double[] verty, double testx, double testy)
    {
        int nvert = vertx.length;
        int i, j;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    }

    void changeAppTitle(String title) {
        surface.setTitle(title);
    }
}
