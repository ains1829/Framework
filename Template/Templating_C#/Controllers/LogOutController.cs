namespace Controllers;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using Model;
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Http;
[ApiController]
[Route("/logout/[controller]")]
public class LogOutController : ControllerBase{
  [HttpPost("deconnexion")]
  public IActionResult Deconnexion(){
      HttpContext.Session.Clear() ;
      return Ok("LOG OUT");
  }
}