//
//  ProfileViewController.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import "PageViewController.h"
#import "UIImageView+WebCache.h"
#import "FeedDisplayViewController.h"
#import "SUCache.h"
#import "SingletonClass.h"

@interface PageViewController ()

@end

@implementation PageViewController

- (void)viewDidLoad {
    self.categoryArray = [[NSMutableArray alloc]init];
    self.nameArray = [[NSMutableArray alloc]init];
    self.idArray = [[NSMutableArray alloc]init];
    
    [self fetchUserLikedPages];
    [self createFollowTable];
    // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchUserLikedPages) name:@"CurrentUserChangedNotification" object:nil];
    self.navigationController.navigationBarHidden=YES;
    
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
}


-(void)createFollowTable
{
    feedTable=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width,self.view.frame.size.height-50) style:UITableViewStylePlain];
    feedTable.dataSource=self;
    feedTable.delegate=self;
    feedTable.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:feedTable];
//     [feedTable reloadData];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    NSString *picUrl =[self.idArray objectAtIndex:indexPath.row];
    

    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",picUrl]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
        //cell.userNameDesc.frame=CGRectMake(150, 5, 150, 100);
        
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",picUrl]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *str1 =[self.nameArray objectAtIndex:indexPath.row];
    
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        cell.userNameDesc.text=@"nothing";
    }
    cell.userNameDesc.numberOfLines=0;
    cell.userNameDesc.lineBreakMode=NSLineBreakByWordWrapping;
//    [cell.userNameDesc sizeToFit];
    NSString *str2 = [self.categoryArray objectAtIndex:indexPath.row];
    cell.fromLabel.text = str2;
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSIndexPath *indexPaths=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPaths.row+indexPaths.section].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
    
    
    NSString *accessToken1=token.tokenString;
    NSString *str1 =[self.idArray objectAtIndex:indexPath.row];
    
    NSString * urlStr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/feed?access_token=%@",str1,accessToken1];
    
    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url=[NSURL URLWithString:urlStr2];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
    NSString *str2=  [self.idArray objectAtIndex:indexPath.row];
    
        if (self.disp) {
            self.disp=nil;
        }
         self.disp = [[FeedDisplayViewController alloc]init];
    self.disp.idStr=str2;
    
    self.navigationController.navigationBarHidden=NO;
    [self.navigationController pushViewController:self.disp animated:YES];
    }
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 1;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.idArray.count;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 250;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)fetchUserLikedPages{
    if (self.categoryArray&&self.nameArray&&self.idArray) {
        self.idArray=nil;
       self.nameArray=nil;
        self.categoryArray=nil;
    }
    
    self.categoryArray=[[NSMutableArray alloc] init];
     self.nameArray=[[NSMutableArray alloc] init];
     self.idArray=[[NSMutableArray alloc] init];
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    

    NSIndexPath *indexpath=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCache itemForSlot:indexpath.row+indexpath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/likes" parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//                              NSLog(@"result is %@",result);
            
                              
                              for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                                  
                                  NSString *str1 = [dic objectForKey:@"category"];
                                  NSString *str2 = [dic objectForKey:@"name"];
                                  NSString *str3= [dic objectForKey:@"id"];
                                  
                                  if (str1) {
                                      [self.categoryArray addObject:str1];
                                  }
                                  else{
                                      [self.categoryArray addObject:@"nil"];
                                  }
                                  
                                  if (str2) {
                                      [self.nameArray addObject:str2];
                                  }else{
                                      [self.nameArray addObject:@"nil"];
                                  }
                                  
                                  if (str3) {
                                      [self.idArray addObject:str3];
                                  }else{
                                      [self.idArray addObject:@"nil"];
                                  }
                                  
                              }
            [feedTable reloadData];
        }];
    }
}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
