var dw = "Product";
var dj = "OneLine";
var log;
var t;
var L;
var be;
var bQ;
var bI;
var by;
var G;
var aY;
var aN;
var aI;
var aV;
var ay;
var bA;
var af;
var bw;
var bu;
var aa;
var bn;
var bz;
var aH;
var aB;
var aJ;
var bO;
var aR;
var kPX;
var kPW;
var C;
var V;
var av;
var aM;
var aZ;
var ab;
var aX;
var aT;
var bP;
var am;
var bB;
var cv;
var aQ;
var lastMillisec;
var bs;
var bN;
var bS;
var ah;
var ai;
var ad;
var ae;
var bx;
var bk;
var aA;
var ar;
var bf;
var cQ;
var bv;
var bo;
var aE;
var au;
var bC;
var bL;
var aC;
var aP;
var bc;
var bE;
var ba;
var bJ;
var as;
var aw;
var ao;
var bH;
var bg;
function k(aq) {
  if ("Development" == dw) {
    if ("OneLine" == dj)log.innerHTML = aq; else if ("Cascade" == dj)log.innerHTML += "<br/>" + aq;
  }
};
function Initialize() {
  if ("Development" == dw) {
    log = document.getElementById("Log");
  }
  t = document.getElementById("CanvasBG");
  be = t.getContext("2d");
  L = document.getElementById("CanvasUI");
  bQ = L.getContext("2d");
  by = null;
  G = null;
  aN = null;
  aI = false;
  aV = true;
  bA = null;
  bu = 0;
  ai = 0;
  ad = 0;
  ae = 0;
  bx = 0;
  bk = ai;
  aA = ad;
  ar = 0;
  bf = 0;
  cQ = 3;
  aE = 1.0;
  au = aE;
  bc = bgR;
  bE = bgG;
  ba = bgB;
  ay = false;
  bJ = -1;
  as = -1;
  aY = null;
  aw = 0;
  bg = 0;
  bI = cj.bd();
};function LoadPhotos(cl, cL, de, dO, dG) {
  af = de;
  bw = cL;
  aa = 0;
  bn = 0;
  bz = aa;
  aH = bn;
  aB = -1;
  aJ = -1;
  bO = dO;
  aR = dG;
  aC = 0;
  aP = 0;
  bB = 0;
  cv = 1.5;
  aQ = 0;
  lastMillisec = 0;
  var dr = 360 / af;
  cQ *= dr;
  XBD();
  F();
  H();
  bA = [];
  for (var j = 0; j < bw; j++) {
    bA[j] = [];
    for (var i = 0; i < af; i++) {
      bA[j][i] = new Image();
      bA[j][i].src = cl + j + "_" + i + ".jpg";
      if (bA[j][i].complete) {
        bu++;
        by.dv(bu / (af * bw) * 100);
      } else {
        bA[j][i].onload = function () {
          bu++;
          by.dv(bu / (af * bw) * 100);
        }
      }
    }
  }
  window.onorientationchange = R;
  window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
  requestAnimationFrame(l);
};function XBD() {
  var ag = document.documentElement.clientWidth;
  var aW = document.documentElement.clientHeight;
  t.width = ag;
  t.height = aW;
  L.width = ag;
  L.height = aW;
  am = t.width / t.height;
  bP = bO / aR;
  if (am > bP) {
    V = t.height;
    C = (V * bP + 0.5) | 0;
    kPX = (((t.width - C) >> 1) + 0.5) | 0;
    kPW = 0;
    bL = aR / t.height;
  } else if (am < bP) {
    C = t.width;
    V = (C / bP + 0.5) | 0;
    kPX = 0;
    kPW = (((t.height - V) >> 1) + 0.5) | 0;
    bL = bO / t.width;
  } else {
    C = t.width;
    V = t.height;
    kPX = 0;
    kPW = 0;
    bL = bO / t.width;
  }
  aZ = C;
  ab = V;
  av = C;
  aM = V;
  au = 1.0;
  bC = 4;
  bC *= bL;
  ai = 0;
  ad = 0;
  ae = 0;
  bx = 0;
  bk = ai;
  aA = ad;
  ar = 0;
  bf = 0;
  var cI = L.width > L.height ? L.height : L.width;
  bv = cQ * cI / 768;
  aB = -1;
  aJ = -1;
  if (by) {
    by.bb(L.width >> 2, 20, L.width >> 1, L.height >> 4, 1);
  }
  if (G) {
    var aD = L.width > L.height ? L.height : L.width;
    var bD = aD / 5;
    var aj = bD;
    G.bb((L.width - bD) >> 1, L.height - aj * 1.2, bD, aj, 1);
  }
};function K(ac, bG) {
  if (0 < ac) {
    bz++;
  } else if (0 > ac) {
    bz--;
  }
  if (0 < bG) {
    aH++;
  } else if (0 > bG) {
    aH--;
  }
  if (0 > bz)bz += af; else if (af <= bz)bz = bz - af;
  if (0 > aH) {
    aH = 0;
    bG = 0;
  } else if (bw <= aH) {
    aH = bw - 1;
    bG = 0;
  }
  if ((aa != bz || bn != aH) && !bA[aH][bz].complete) {
    K(-ac, -bG);
  }
};function v(cf, df) {
  if (C < t.width || V < t.height) {
    be.fillStyle = "rgb(" + bc + ", " + bE + ", " + ba + ")";
    be.fillRect(0, 0, t.width, t.height);
  }
  be.drawImage(bA[df][cf], kPX, kPW, C, V);
};function O() {
  var ax = true;
  if (am <= bP) {
    if (C < t.width) {
      C = t.width;
      V = C / bP;
      au = 1;
      ax = false;
    }
  } else {
    if (V < t.height) {
      V = t.height;
      C = V * bP;
      au = 1;
      ax = false;
    }
  }
  return ax;
};function J() {
  if (bP >= am) {
    if (0 < kPX)kPX = 0; else if (kPX + C < t.width)kPX = t.width - C;
    if (V < t.height) {
      kPW = (t.height - V) >> 1;
    } else {
      if (0 < kPW)kPW = 0; else if (kPW + V < t.height)kPW = t.height - V;
    }
  } else {
    if (0 < kPW)kPW = 0; else if (kPW + V < t.height)kPW = t.height - V;
    if (C < t.width) {
      kPX = (t.width - C) >> 1;
    } else {
      if (0 < kPX)kPX = 0; else if (kPX + C < t.width)kPX = t.width - C;
    }
  }
};function DYQ(aK) {
  au *= aK;
  if (bC < au)au = bC; else {
    av = C;
    aM = V;
    C *= aK;
    V *= aK;
    if (!O()) {
      J();
      v(bz, aH);
      return;
    }
    var deltaX = (C - av) * aX / aZ;
    var deltaY = (V - aM) * aT / ab;
    kPX -= deltaX;
    kPW -= deltaY;
    J();
  }
};function l() {
  if (!aI && 0 != bB) {/*aQ+=bB*cv;if(bv<Math.abs(aQ)){K(aQ,0);aQ=0;}*/
    var dateNow = new Date();
    var millisec = dateNow.getMilliseconds();
    var timeInterval = (millisec - lastMillisec + 1000) % 1000;
    if (1000 / fps < timeInterval) {
      K(bB);
      lastMillisec = millisec;
    }
  }
  if (!aI && (0 != bk || 0 != aA)) {
    var bm = 0.9;
    if (0 < bk) {
      bk *= bm;
      if (0.1 > bk)bk = 0;
    } else if (0 > bk) {
      bk *= bm;
      if (-0.1 < bk)bk = 0;
    }
    if (0 < aA) {
      aA *= bm;
      if (0.1 > aA)aA = 0;
    } else if (0 > aA) {
      aA *= bm;
      if (-0.1 < aA)aA = 0;
    }
    if (1 >= au) {
      ar += bk;
      bf += aA;
      if (bv < Math.abs(ar)) {
        K(ar, 0);
        ar = 0;
      }
      if (bv < Math.abs(bf)) {
        K(0, bf);
        bf = 0;
      }
    } else {
      kPX += bk;
      kPW += aA;
      J();
    }
  }
  if (1 == bg) {
    DYQ(1.07);
    if (C > 2 * bO)bg = 0;
  } else if (-1 == bg) {
    DYQ(0.93);
    if (1 >= au)bg = 0;
  }
  if (1 >= au && aB == bz && aJ == aH) {
  } else if (bA && bA[aH][bz].complete) {
    v(bz, aH);
    aB = bz;
    aJ = aH;
  }
  bI.ca();
  requestAnimationFrame(l);
};function R() {
  switch (window.orientation) {
    case 0:
    case-90:
    case 90:
    case 180:
      setTimeout("XBD()", 300);
      ;
      break;
  }
};function TouchDown(event) {
  event = event || windows.event;
  event.preventDefault();
  aI = true;
  clearTimeout(aN);
  G.bb((L.width - G.h.width) >> 1, L.height - G.h.width * 1.2, G.h.width, G.h.height, 10);
  if (1 == event.touches.length) {
    bs = event.touches[0].pageX;
    bN = event.touches[0].pageY;
    bJ = bs;
    as = bN;
    aY = bI.bV(bs, bN);
    if (!aY) {
      co = 0;
      dh = 0;
      bk = 0;
      aA = 0;
      aw++;
      if (1 == aw) {
        ao = bs;
        bH = bN;
        setTimeout(function () {
          aw = 0;
        }, 400);
      } else if (2 == aw) {
        var bp = Math.sqrt(Math.pow(bs - ao, 2) + Math.pow(bN - bH, 2));
        if (50 > bp) {
          aX = bs - kPX;
          aT = bN - kPW;
          aZ = C;
          ab = V;
          if (1.2 >= au)bg = 1; else bg = -1;
        }
        aw = 0;
      }
    }
  } else if (2 == event.touches.length) {
    aw = 0;
    bs = event.touches[0].pageX;
    bN = event.touches[0].pageY;
    bS = event.touches[1].pageX;
    ah = event.touches[1].pageY;
    bo = Math.pow((Math.pow((bS - bs), 2) + Math.pow((ah - bN), 2)), 0.5);
    aC = (bs + bS) >> 1;
    aP = (bN + ah) >> 1;
    aX = aC - kPX;
    aT = aP - kPW;
    aZ = C;
    ab = V;
  }
};function TouchMove(event) {
  event = event || window.event;
  event.preventDefault();
  if (1 == event.touches.length) {
    if (aV) {
      var x = event.touches[0].pageX;
      var y = event.touches[0].pageY;
      bJ = x;
      as = y;
      if (1 == aw) {
        var bp = Math.sqrt(Math.pow(x - ao, 2), Math.pow(y - bH, 2));
        if (40 < bp)aw = 0;
      }
      var ac = x - bs;
      var bG = y - bN;
      ai = ac;
      ad = bG;
      ae += ai;
      bx += ad;
      if (1 == au) {
        if (bv < Math.abs(ae)) {
          if (0 < ae)bB = Math.abs(bB); else bB = -Math.abs(bB);
          K(ae, 0);
          ae = 0;
        }
        if (bv < Math.abs(bx)) {
          K(0, bx);
          bx = 0;
        }
      } else {
        kPX += ac;
        kPW += bG;
        J();
      }
      bs = x;
      bN = y;
    }
  } else if (2 == event.touches.length) {
    var cn = event.touches[0].pageX;
    var ci = event.touches[0].pageY;
    var x1 = event.touches[1].pageX;
    var y1 = event.touches[1].pageY;
    var bp = Math.sqrt(Math.pow((x1 - cn), 2) + Math.pow((y1 - ci), 2));
    var aK = bp / bo;
    bo = bp;
    DYQ(aK);
  }
};function TouchUp(event) {
  event = event || window.event;
  if (2 > event.touches.length) {
    aE = au;
    if (1 == event.touches.length) {
      bs = event.touches[0].pageX;
      bN = event.touches[0].pageY;
      aV = false;
      setTimeout(function () {
        aV = true;
      }, 200);
    } else if (0 == event.touches.length) {
      var OKC = bI.bV(bJ, as);
      if (OKC && aY == OKC) {
        OKC.Click();
      }
      if (aV) {
        if ("Pressed" == G.status && 1 >= au) {
          bk = 0;
          aA = 0;
        } else {
          bk = ai;
          aA = ad;
        }
        ai = 0;
        ad = 0;
      } else aV = true;
      aI = false;
    }
  }
  aN = setTimeout(function () {
    G.bb((L.width - G.h.width) >> 1, L.height, G.h.width, G.h.height, 10);
  }, 2000);
};
function MouseDown(event) {
  event = event || windows.event;
  event.preventDefault();
  aI = true;
  clearTimeout(aN);
  G.bb((L.width - G.h.width) >> 1, L.height - G.h.width * 1.2, G.h.width, G.h.height, 10);
  bs = event.clientX;
  bN = event.clientY;
  bJ = bs;
  as = bN;
  aY = bI.bV(bs, bN);
  if (!aY) {
    co = 0;
    dh = 0;
    bk = 0;
    aA = 0;
    aw++;
    if (1 == aw) {
      ao = bs;
      bH = bN;
      setTimeout(function () {
        aw = 0;
      }, 400);
    } else if (2 == aw) {
      var bp = Math.sqrt(Math.pow(bs - ao, 2) + Math.pow(bN - bH, 2));
      if (50 > bp) {
        aX = bs - kPX;
        aT = bN - kPW;
        aZ = C;
        ab = V;
        if (1.2 >= au)bg = 1; else bg = -1;
      }
      aw = 0;
    }
  }
};function MouseMove(event) {
  aZ = C;
  ab = V;
  aC = event.clientX;
  aP = event.clientY;
  aX = aC - kPX;
  aT = aP - kPW;
  if (!aI)return;
  event = event || window.event;
  event.preventDefault();
  if (aV) {
    var x = event.clientX;
    var y = event.clientY;
    bJ = x;
    as = y;
    if (1 == aw) {
      var bp = Math.sqrt(Math.pow(x - ao, 2), Math.pow(y - bH, 2));
      if (40 < bp)aw = 0;
    }
    var ac = x - bs;
    var bG = y - bN;
    ai = ac;
    ad = bG;
    ae += ai;
    bx += ad;
    if (1 >= au) {
      if (bv < Math.abs(ae)) {
        if (0 < ae)bB = Math.abs(bB); else bB = -Math.abs(bB);
        K(ae, 0);
        ae = 0;
      }
      if (bv < Math.abs(bx)) {
        K(0, bx);
        bx = 0;
      }
    } else {
      kPX += ac;
      kPW += bG;
      J();
    }
    bs = x;
    bN = y;
  }
};function MouseUp(event) {
  event = event || window.event;
  aE = au;
  var OKC = bI.bV(bJ, as);
  if (OKC && aY == OKC) {
    OKC.Click();
  }
  if (aV) {
    if ("Pressed" == G.status && 1 >= au) {
      bk = 0;
      aA = 0;
    } else {
      bk = ai;
      aA = ad;
    }
    ai = 0;
    ad = 0;
  } else aV = true;
  aI = false;
  aN = setTimeout(function () {
    G.bb((L.width - G.h.width) >> 1, L.height, G.h.width, G.h.height, 10);
  }, 2000);
};function MouseWheel(event) {
  event = event || window.event;
  event.preventDefault();
  var delta = event.wheelDelta || event.deltaX || event.deltaY;
  aX = event.clientX - kPX;
  aT = event.clientY - kPW;
  if (0.0 < delta) {
    DYQ(1.15);
  } else if (0.0 > delta) {
    DYQ(0.85);
  }
};function F() {
  var x = (L.width >> 1) - 200;
  var y = 10;
  var w = 400;
  var h = 30;
  if (0 > x) {
    x = 0;
    w = L.width;
  }
  by = du.bd(x, y, w, h);
  bI.dz(by);
};function H() {
  var aD = L.width > L.height ? L.height : L.width;
  var bD = aD / 8;
  var aj = bD;
  G = OKQ.bd((L.width - bD) >> 1, L.height - aj * 1.2, bD, aj, "/images/ProductDetail/UI/AutoSpinNormal.png", "/images/ProductDetail/UI/AutoSpinPressed.png");
  G.OnClick = function () {
    if (0 == bB) {
      if (0 <= bk)bB = 7; else bB = -7;
    } else bB = 0;
  };
  bI.dz(G);
};function T(x, y, width, height) {
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
};var URW = {
  bd: function (x, y, width, height) {
    x = (x + 0.5) | 0;
    y = (y + 0.5) | 0;
    width = (width + 0.5) | 0;
    height = (height + 0.5) | 0;
    var OKC = {};
    OKC.h = new T(x, y, width, height);
    OKC.ap = new T(x, y, width, height);
    OKC.bU = new T(x, y, width, height);
    OKC.bi = new T(x, y, width, height);
    OKC.al = 0;
    OKC.aS = 0;
    OKC.bM = false;
    OKC.visible = true;
    OKC.enable = true;
    OKC.aG = null;
    OKC.dg = function () {
      OKC.visible = true;
      OKC.bM = true;
    };
    OKC.cZ = function () {
      OKC.visible = false;
      OKC.bM = true;
    };
    OKC.bb = function (cP, dE, dN, dn, aS) {
      OKC.bU.x = OKC.h.x;
      OKC.bU.y = OKC.h.y;
      OKC.bU.width = OKC.h.width;
      OKC.bU.height = OKC.h.height;
      OKC.bi.x = (cP + 0.5) | 0;
      OKC.bi.y = (dE + 0.5) | 0;
      OKC.bi.width = (dN + 0.5) | 0;
      OKC.bi.height = (dn + 0.5) | 0;
      OKC.aS = aS;
      OKC.al = 0;
      cJ();
      OKC.bM = true;
    };
    var cJ = function () {
      OKC.al++;
      var aO = OKC.al / OKC.aS;
      var dM = OKC.bU.x + (OKC.bi.x - OKC.bU.x) * aO;
      var dk = OKC.bU.y + (OKC.bi.y - OKC.bU.y) * aO;
      var cp = OKC.bU.width + (OKC.bi.width - OKC.bU.width) * aO;
      var ck = OKC.bU.height + (OKC.bi.height - OKC.bU.height) * aO;
      OKC.h.x = (dM + 0.5) | 0;
      OKC.h.y = (dk + 0.5) | 0;
      OKC.h.width = (cp + 0.5) | 0;
      OKC.h.height = (ck + 0.5) | 0;
    };
    OKC.cq = function () {
      bQ.clearRect(OKC.ap.x, OKC.ap.y, OKC.ap.width, OKC.ap.height);
      OKC.ap.x = OKC.h.x;
      OKC.ap.y = OKC.h.y;
      OKC.ap.width = OKC.h.width;
      OKC.ap.height = OKC.h.height;
      if (OKC.visible) {
        OKC.aG();
      }
      if (OKC.al >= OKC.aS)OKC.bM = false; else cJ();
    };
    return OKC;
  }
};
var OKQ = {
  bd: function (x, y, width, height, cz, cX) {
    x = (x + 0.5) | 0;
    y = (y + 0.5) | 0;
    width = (width + 0.5) | 0;
    height = (height + 0.5) | 0;
    var VJW = URW.bd(x, y, width, height);
    VJW.type = "Switch";
    VJW.status = "Normal";
    var aF = null;
    var bF = null;
    var bh = null;
    var bq = null;
    if (!cz || !cX) {
      bQ.fillStyle = "#00FFFF";
      bQ.fillRect(x, y, width, height);
    } else {
      aF = new Image();
      aF.src = cz;
      if (aF.complete) {
        bh = document.createElement("canvas");
        bh.width = width;
        bh.height = height;
        var aU = bh.getContext("2d");
        aU.drawImage(aF, 0, 0, bh.width, bh.height);
        bQ.drawImage(bh, x, y, width, height);
      } else {
        aF.onload = function () {
          bh = document.createElement("Canvas");
          bh.width = width;
          bh.height = height;
          var aU = bh.getContext("2d");
          aU.drawImage(aF, 0, 0, bh.width, bh.height);
          bQ.drawImage(bh, x, y, width, height);
        }
      }
      bF = new Image();
      bF.src = cX;
      if (bF.complete) {
        bq = document.createElement("canvas");
        bq.width = width;
        bq.height = height;
        var an = bq.getContext("2d");
        an.drawImage(bF, 0, 0, bq.width, bq.height);
      } else {
        bF.onload = function () {
          bq = document.createElement("Canvas");
          bq.width = width;
          bq.height = height;
          var an = bq.getContext("2d");
          an.drawImage(bF, 0, 0, bq.width, bq.height);
        }
      }
    }
    VJW.da = function () {
      VJW.status = "Pressed";
      VJW.bM = true;
    };
    VJW.dP = function () {
      VJW.status = "Normal";
      VJW.bM = true;
    };
    VJW.Click = function () {
      if ("Switch" == VJW.type) {
        switch (VJW.status) {
          case "Normal":
            VJW.da();
            break;
          case "Pressed":
            VJW.dP();
            break;
        }
      }
      VJW.OnClick();
    };
    VJW.OnClick = null;
    VJW.aG = function () {
      switch (VJW.status) {
        case "Normal":
          if (!aF)return;
          if (aF.complete) {
            bQ.drawImage(bh, VJW.h.x, VJW.h.y, VJW.h.width, VJW.h.height);
          } else {
            aF.onload = function () {
              bQ.drawImage(bh, VJW.h.x, VJW.h.y, VJW.h.width, VJW.h.height);
            }
          }
          break;
        case "Pressed":
          if (!bF)return;
          if (bF.complete) {
            bQ.drawImage(bq, VJW.h.x, VJW.h.y, VJW.h.width, VJW.h.height);
          } else {
            bF.onload = function () {
              bQ.drawImage(bq, VJW.h.x, VJW.h.y, VJW.h.width, VJW.h.height);
            }
          }
          break;
      }
    };
    return VJW;
  }
};
var du = {
  bd: function (x, y, width, height) {
    var ak = URW.bd(x, y, width, height);
    ak.az = 0;
    ak.dv = function (az) {
      ak.az = (az + 0.5) | 0;
      ak.bM = true;
    };
    ak.aG = function () {
      if (!ak.visible) {
        return;
      }
      var aq;
      if (100 <= ak.az) {
        aq = "已加载100%！";
        setTimeout(function () {
          bQ.clearRect(ak.h.x, ak.h.y, ak.h.width, ak.h.height);
          ak.visible = false;
        }, 1200);
      } else {
        aq = "已加载" + ak.az + "%，请稍候…";
      }
      var cPL = ak.az * (ak.h.width - 2) / 100;
      bQ.fillStyle = '#939393';
      bQ.font = '16px Franklin Gothic Medium';
      bQ.fillText('Loading . . .', ak.h.x + ((ak.h.width >> 1) - 30), ak.h.y + 16);
      bQ.fillStyle = 'rgba(179, 179, 179, 0.6)';
      bQ.fillRect(ak.h.x, ak.h.y + 20, ak.h.width, 5);
      bQ.fillStyle = '#FFFFFF';
      bQ.fillRect(ak.h.x + 1 + cPL, ak.h.y + 21, ak.h.width - cPL - 2, 3);
      bQ.fillStyle = 'rgba(152, 204, 243, 0.6)';
      bQ.fillRect(ak.h.x + 1, ak.h.y + 21, cPL, 3);
    };
    return ak;
  }
};
var dK = {
  bd: function (x, y, width, height) {
    var TUW = URW.bd(x, y, width, height);
    TUW.dp = true;
    return TUW;
  }
};
var cj = {
  bd: function () {
    var bI = {};
    var AIF = [];
    var bj = 0;
    bI.dz = function (OKC) {
      AIF[bj++] = OKC;
    };
    bI.bV = function (x, y) {
      for (var i = 0; i < bj; i++) {
        if (!AIF[i].enable || !AIF[i].visible)continue;
        if (AIF[i].h.x <= x && AIF[i].h.x + AIF[i].h.width > x && AIF[i].h.y <= y && AIF[i].h.y + AIF[i].h.height > y) {
          return AIF[i];
        }
      }
      return null;
    };
    bI.ca = function () {
      for (var i = 0; i < bj; i++) {
        if (AIF[i].bM) {
          AIF[i].cq();
        }
      }
    };
    return bI;
  }
};

export { Initialize, LoadPhotos, MouseDown, MouseMove, MouseUp, MouseWheel };